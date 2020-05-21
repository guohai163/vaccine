package org.guohai.vaccine.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineAccessLog;
import org.guohai.vaccine.beans.WechatUserBean;
import org.guohai.vaccine.beans.WechatUserInfoBean;
import org.guohai.vaccine.dao.VaccineDao;
import org.guohai.vaccine.utilities.WxCryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
@Component
@ConfigurationProperties
public class MiniProgramServiceImpl implements MiniProgramService {

    private static final Logger LOG  = LoggerFactory.getLogger(MiniProgramServiceImpl.class);

    @Autowired
    VaccineDao vaccineDao;

    @Value("${my-data.wechat-mini.appid}")
    private String appid;

    @Value("${my-data.wechat-mini.appsecret}")
    private String appsecret;

    /**
     * 已登录用户数据，String存储为loginCode
     */
    public static HashMap<String, WechatUserBean> UserMap = new HashMap<>();


    private static String AccessToken="";

    private static Date ATExpTime = new Date() ;

    /*** 小程序登录
     *
     * @param code
     * @return
     */
    @Override
    public Result<String> oalogin(String code,String src, String sharedData, String iv) {
        String requestWCUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",appid,appsecret,code);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(requestWCUrl);
        CloseableHttpResponse response = null;
        JSONObject wcResult = null;
        try{
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                wcResult = new JSONObject(new JSONTokener(new InputStreamReader(response.getEntity().getContent(), EntityUtils.getContentCharSet())))
                HttpEntity httpEntity = response.getEntity();
                if(null!= httpEntity) {
                    BufferedReader buff = new BufferedReader(new InputStreamReader(httpEntity.getContent(),"UTF-8"),8*1024);
                    String line =null;
                    StringBuilder strResult = new StringBuilder();
                    while ((line = buff.readLine())!=null){
                        strResult.append(line+"/n");
                    }
                    wcResult = new JSONObject(strResult.toString());
                    //{"session_key":"EF5qJaPcIUj5yV5\/ascaig==","openid":"o6pfI5Q3R1ugslEcwexMpYDZ2WDg"
                    if(wcResult.optString("errcode").equals("")){
                        // 请求成功
                        WechatUserBean wechatUserBean = new WechatUserBean(wcResult.get("openid").toString(),code,wcResult.get("session_key").toString(), src,new Date(), "");
                        LOG.info(String.format("微信登录请求结果：%s",wcResult));
                        if(!sharedData.isEmpty()) {
                            // 解密分享的内容
                            String shared = WxCryptUtils.decrypt(sharedData, iv, wechatUserBean.getSessionKey());
                            LOG.info(String.format("分享数据信息：%s", shared));
                            wechatUserBean.setSharedData(shared);
                        }


                        WechatUserBean dbOldUser = vaccineDao.getUserByOpenId(wechatUserBean.getOpenId());
                        if(dbOldUser == null) {
                            vaccineDao.addUser(wechatUserBean);
                            UserMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
                        }else {
                            UserMap.remove(dbOldUser.getLoginCode());

                            UserMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
                            vaccineDao.setUser(wechatUserBean);
                        }
                    }
                    else {
                        // 请求失败
                        return new Result<>(false,wcResult.get("errmsg").toString());
                    }

                }
            }
        }catch (Exception e){
            LOG.error(e.toString());
            new Result<>(false,e.toString());
        }
        return new Result<>(true,"OK");
    }

    public byte[] getWcaCode(String src) {
        if(AccessToken == null ||AccessToken.length()==0 || new Date().after(ATExpTime)){
            getWCAccessToken();

        }
        String url = String.format("https://api.weixin.qq.com/wxa/getwxacode?access_token=%s",AccessToken);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        String request = String.format("{\n" +
                " \"path\":\"pages/index/index?src=%s\",\n" +
                " \"width\":360\n" +
                "}",src);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(request,"UTF-8"));
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toByteArray(httpEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 获取来访日志
     *
     * @return
     */
    @Override
    public List<VaccineAccessLog> getAccessLogs() {
        return vaccineDao.getAccessLog();
    }

    /**
     * 获取用户列表
     *
     * @return
     */
    @Override
    public List<WechatUserBean> getUserList() {
        return vaccineDao.getUserList();
    }

    /**
     * 检查loginCode是否有效
     *
     * @param loginCode
     * @return
     */
    @Override
    public Result<String> checkLoginCode(String loginCode) {
        if(UserMap.get(loginCode) != null){
            return new Result<>(true, "ok");
        }
        WechatUserBean wechatUserBean = vaccineDao.getUserByLoginCode(loginCode);
        if(wechatUserBean!=null){
            UserMap.put(loginCode,wechatUserBean);
            return new Result<>(true, "ok");
        }
        return new Result<>(false, "no login code");
    }

    /**
     * @param userInfo
     * @return
     */
    @Override
    public Result<String> putUserMoreInfo(WechatUserInfoBean userInfo) {
        WechatUserBean userbean = vaccineDao.getUserByLoginCode(userInfo.getLoginCode());
        if(userbean != null){
            userInfo.setOpenId(userbean.getOpenId());
            try {
                vaccineDao.addUserData(userInfo);
            }
            catch (Exception ex){
                LOG.error(ex.toString());
                return new Result<>(false,ex.toString());
            }

        }

        return new Result<>(true,"success");
    }

    /**
     * 阿里小程序登录
     *
     * @param code
     * @return
     */
    @Override
    public Result<String> aliMiniLogin(String code) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","utf-8","alipay_public_key","RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return null;
    }

    private Boolean getWCAccessToken(){
        String getUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",appid,appsecret);
        try {
            JSONObject result = httpGet(getUrl);
            AccessToken = result.getString("access_token");
            ATExpTime = new Date(new Date().getTime()+result.getLong("expires_in")* 1000L);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private JSONObject httpGet(String url) throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        JSONObject wcResult = null;
        response = httpClient.execute(httpGet);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity httpEntity = response.getEntity();
            if(null!= httpEntity) {
                BufferedReader buff = new BufferedReader(new InputStreamReader(httpEntity.getContent(),"UTF-8"),8*1024);
                String line =null;
                StringBuilder strResult = new StringBuilder();
                while ((line = buff.readLine())!=null){
                    strResult.append(line+"/n");
                }
                wcResult = new JSONObject(strResult.toString());

                }
        }
        LOG.info(String.format("rquest url: %s, result is: %s",url,wcResult.toString()));
        return wcResult;
    }

}

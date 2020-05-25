package org.guohai.vaccine.service;

import com.alipay.easysdk.base.oauth.models.AlipaySystemOauthTokenResponse;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.BaseClient.Config;


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

    @Value("${my-data.alipay-mini.appid}")
    private String alipayAppId;

    @Value("${my-data.alipay-mini.merchantPrivateKey}")
    private String alipayMerchantPrivateKey;

    @Value("${my-data.alipay-mini.alipayPublicKey}")
    private  String alipayPublicKey;

    /**
     * 已登录用户数据，String存储为loginCode
     */
    public static HashMap<String, WechatUserBean> UserMap = new HashMap<>();


    private static String AccessToken="";

    private static Date ATExpTime = new Date() ;

    private Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";

        config.appId = alipayAppId;

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayMerchantPrivateKey;

        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
//        config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
//        config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
//        config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";

        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
         config.alipayPublicKey = alipayPublicKey;

        //可设置异步通知接收服务地址（可选）
//        config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";

        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
//        config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

        return config;
    }

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
    public AlipaySystemOauthTokenResponse aliMiniLogin(String code) {
        // TODO: ali
        Factory.setOptions(getOptions());
        AlipaySystemOauthTokenResponse response = null;
        try {
            response = Factory.Base.OAuth().getToken(code);
            WechatUserBean wechatUserBean = new WechatUserBean(response.userId,code,response.accessToken,"alipay",new Date(),"");
            WechatUserBean dbOldUser = vaccineDao.getUserByOpenId(wechatUserBean.getOpenId());
            if(dbOldUser == null) {
                vaccineDao.addUser(wechatUserBean);
                UserMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
            }else {
                UserMap.remove(dbOldUser.getLoginCode());

                UserMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
                vaccineDao.setUser(wechatUserBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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

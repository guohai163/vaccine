package org.guohai.vaccine.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.WechatUserBean;
import org.guohai.vaccine.dao.VaccineDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;


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
    public static HashMap<String, WechatUserBean> userMap = new HashMap<>();


    /*** 小程序登录
     *
     * @param code
     * @return
     */
    @Override
    public Result<String> oalogin(String code,String src) {
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
                    WechatUserBean wechatUserBean = new WechatUserBean(wcResult.get("openid").toString(),code,wcResult.get("session_key").toString(), src,new Date());
                    LOG.info(String.format("微信登录请求结果：%s",wcResult));
                    WechatUserBean dbOldUser = vaccineDao.getUserByOpenId(wechatUserBean.getOpenId());
                    if(dbOldUser == null) {
                        vaccineDao.addUser(wechatUserBean);
                        userMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
                    }else {
                        userMap.remove(dbOldUser.getLoginCode());

                        userMap.put(wechatUserBean.getLoginCode(),wechatUserBean);
                        vaccineDao.setUser(wechatUserBean);
                    }
                }
            }
        }catch (Exception e){
            LOG.error(e.toString());
            new Result<>(false,e.toString());
        }
        return new Result<>(true,"OK");
    }
}

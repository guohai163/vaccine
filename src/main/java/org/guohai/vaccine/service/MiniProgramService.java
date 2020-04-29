package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineAccessLog;
import org.guohai.vaccine.beans.WechatUserBean;
import org.guohai.vaccine.beans.WechatUserInfoBean;

import java.util.List;

public interface MiniProgramService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    Result<String> oalogin(String code,String src);

    /**
     *
     * 活的微信小程序展示用二维码
     * @param src
     * @return
     */
    byte[] getWcaCode(String src);

    /**
     *
     * 获取来访日志
     * @return
     */
    List<VaccineAccessLog> getAccessLogs();

    /**
     * 获取用户列表
     * @return
     */
    List<WechatUserBean> getUserList();

    /**
     * 检查loginCode是否有效
     * @param loginCode
     * @return
     */
    Result<String> checkLoginCode(String loginCode);

    /**
     *
     * @param userInfo
     * @return
     */
    Result<String> putUserMoreInfo(WechatUserInfoBean userInfo);
}

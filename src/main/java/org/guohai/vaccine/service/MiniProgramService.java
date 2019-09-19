package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineAccessLog;

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
}

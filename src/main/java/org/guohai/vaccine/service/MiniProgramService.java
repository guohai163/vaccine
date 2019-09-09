package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.Result;

public interface MiniProgramService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    Result<String> oalogin(String code,String src);

    byte[] getWcaCode(String src);
}

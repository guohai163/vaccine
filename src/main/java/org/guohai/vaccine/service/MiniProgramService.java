package org.guohai.vaccine.service;

import org.guohai.vaccine.org.guohai.vaccine.beans.Result;

public interface MiniProgramService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    Result<String> oalogin(String code);
}

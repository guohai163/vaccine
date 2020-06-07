package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineUserInfoBean;

public interface AdminService {

    Result<VaccineUserInfoBean> getUserInfoByCode(String userCode);
}

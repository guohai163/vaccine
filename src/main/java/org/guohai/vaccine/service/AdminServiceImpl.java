package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineUserInfoBean;
import org.guohai.vaccine.dao.VaccineUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    VaccineUserDao vaccineUserDao;

    @Override
    public Result<VaccineUserInfoBean> getUserInfoByCode(String userCode) {
        VaccineUserInfoBean userInfo = vaccineUserDao.getUserInfoByUserCode(userCode);
        if(null == userInfo) {
            return new Result<>(false, null);
        }
        return new Result<>(true, userInfo);
    }
}

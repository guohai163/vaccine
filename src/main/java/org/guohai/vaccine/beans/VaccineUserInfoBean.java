package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/***
 * 记录用户更多信息使用
 */
@Data
@AllArgsConstructor
public class VaccineUserInfoBean {
    private String userCode;
    private String gender;
    private String avatar;
    private String city;
    private String country;
    private String nickName;
    private String province;
    private Date createDate;
}

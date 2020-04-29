package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WechatUserInfoBean {

    private String openId;

    private String loginCode;

    private String gender;

    private String city;
}

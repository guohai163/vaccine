package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class WechatUserBean {

    private String openId;

    private String loginCode;

    private String sessionKey;

    private String src;

    private Date createTime;
}

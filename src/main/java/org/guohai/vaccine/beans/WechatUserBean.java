package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WechatUserBean {

    private String openId;

    private String loginCode;

    private String sessionKey;

    private String chan;
}

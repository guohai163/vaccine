package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class VaccineAccessLog {

    private Integer code;

    private String openId;

    private String userAgent;

    private String userIp;

    private String serviceCategory;

    private String queryParam;

    private Date accessDate;
}

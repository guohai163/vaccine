package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineBatchBean {

    /**
     * 数据库编号
     */
    private Integer code;

    /**
     * 页面内序号
     */
    private String pageCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 规格
     */
    private String norm;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 签发量
     */
    private String batchNum;

    /**
     * 有效期至
     */
    private String expDate;

    /**
     * 生产企业
     */
    private String manufacturer;

    /**
     * 收检编号
     */
    private String inspectionNum;

    /**
     * 证书编号
     */
    private String certificateNo;

    /**
     * 报告编号
     */
    private String reportNum;

    /**
     * 签发日期
     */
    private String issueDate;

    /**
     * 签发结论
     */
    private String issueConclusion;

    /**
     * 批签发机构
     */
    private String batchIssuingAgency;

    /**
     * 页面编号
     */
    private Integer urlCode;

    /**
     * 批次URL
     */
    private String batchUrl;

    /**
     * 批次名
     */
    private String batchName;
}

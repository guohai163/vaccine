package org.guohai.vaccine.dao;


import org.apache.ibatis.annotations.*;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineUrlBean;

public interface VaccineDao {

    @Insert("INSERT INTO `vaccine_url_tb`\n" +
            "(`batch_url`,`batch_name`)\n" +
            "VALUES\n" +
            "(#{batch_url.batchUrl},#{batch_url.batchName});\n")
    @Options(useGeneratedKeys = true, keyProperty = "batch_url.code")
    Boolean addBatchUrl(@Param("batch_url") VaccineUrlBean batchUrl);

    @Select("SELECT count(*) FROM vaccine_url_tb WHERE batch_url=#{batchUrl}")
    Integer getBatchUrlCount(@Param("batchUrl") String batchUrl);

    @Insert("INSERT INTO `vaccine_batch_tb`\n" +
            "(`page_code`,\n" +
            "`product_name`,\n" +
            "`norm`,\n" +
            "`batch_no`,\n" +
            "`batch_num`,\n" +
            "`exp_date`,\n" +
            "`manufacturer`,\n" +
            "`inspection_num`,\n" +
            "`certificate_no`,\n" +
            "`report_num`,\n" +
            "`issue_date`,\n" +
            "`issue_conclusion`,\n" +
            "`batch_issuing_agency`,\n" +
            "`url_code`)\n" +
            "VALUES\n" +
            "(#{batchData.pageCode},\n" +
            "#{batchData.productName},\n" +
            "#{batchData.norm},\n" +
            "#{batchData.batchNo},\n" +
            "#{batchData.batchNum},\n" +
            "#{batchData.expDate},\n" +
            "#{batchData.manufacturer},\n" +
            "#{batchData.inspectionNum},\n" +
            "#{batchData.certificateNo},\n" +
            "#{batchData.reportNum},\n" +
            "#{batchData.issueDate},\n" +
            "#{batchData.issueConclusion},\n" +
            "#{batchData.batchIssuingAgency},\n" +
            "#{batchData.urlCode});\n")
    Boolean addVaccineBatchData(@Param("batchData")VaccineBatchBean batch);

}

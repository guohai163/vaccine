package org.guohai.vaccine.dao;


import org.apache.ibatis.annotations.*;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineUrlBean;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface VaccineDao {

    @Insert("INSERT INTO `vaccine_url_tb`\n" +
            "(`batch_url`,`batch_name`)\n" +
            "VALUES\n" +
            "(#{batch_url.batchUrl},#{batch_url.batchName});\n")
    @Options(useGeneratedKeys = true, keyProperty = "batch_url.code")
    Boolean addBatchUrl(@Param("batch_url") VaccineUrlBean batchUrl);

    @Select("SELECT count(*) FROM vaccine_url_tb WHERE batch_url=#{batchUrl}")
    Integer getBatchUrlCount(@Param("batchUrl") String batchUrl);

    @Select("SELECT vaccine_batch_tb.*,vaccine_url_tb.batch_url,vaccine_url_tb.batch_name " +
            "FROM vaccine_batch_tb join vaccine_url_tb on vaccine_batch_tb.url_code=vaccine_url_tb.code " +
            "WHERE batch_no like #{key} limit 10;")
    List<VaccineBatchBean>  searchVaccine(@Param("key") String vaccineKey);

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
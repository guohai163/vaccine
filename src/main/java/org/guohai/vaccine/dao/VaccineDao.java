package org.guohai.vaccine.dao;


import org.apache.ibatis.annotations.*;
import org.guohai.vaccine.beans.VaccineAccessLog;
import org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.beans.VaccineUrlBean;
import org.guohai.vaccine.beans.WechatUserBean;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VaccineDao {

    @Insert("INSERT INTO `vaccine_url_tb`\n" +
            "(`batch_url`,`batch_name`,`batch_date`)\n" +
            "VALUES\n" +
            "(#{batch_url.batchUrl},#{batch_url.batchName},#{batch_url.batchDate});\n")
    @Options(useGeneratedKeys = true, keyProperty = "batch_url.code")
    Boolean addBatchUrl(@Param("batch_url") VaccineUrlBean batchUrl);

    @Select("SELECT count(*) FROM vaccine_url_tb WHERE batch_url=#{batchUrl}")
    Integer getBatchUrlCount(@Param("batchUrl") String batchUrl);

    @Update("update vaccine_url_tb set batch_date=#{batch.batchDate} WHERE batch_url=#{batch.batchUrl}")
    Boolean updateCatchDate(@Param("batch") VaccineUrlBean batch);

    /**
     * 按疫苗批次进行搜索
     * @param vaccineKey
     * @return
     */
    @Select("SELECT vaccine_batch_tb.*,vaccine_url_tb.batch_url,vaccine_url_tb.batch_name " +
            "FROM vaccine_batch_tb join vaccine_url_tb on vaccine_batch_tb.url_code=vaccine_url_tb.code " +
            "WHERE batch_no like #{key} limit 30;")
    List<VaccineBatchBean>  searchVaccine(@Param("key") String vaccineKey);

    /**
     * 添加疫苗批次数据
     * @param batch
     * @return
     */
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

    @Select("SELECT batch_date FROM vaccine_db.vaccine_url_tb order by code desc limit 1;")
    String getLastDate();

    @Update("update vaccine_config_tb set last_update_date = #{lastDate}")
    Boolean setLastDate(@Param("lastDate") Date lastDate);


    @Insert("INSERT INTO vaccine_wechat_user_tb(open_id,login_code,session_key,src,create_time)VALUES(#{user.openId},#{user.loginCode},#{user.sessionKey},#{user.src},#{user.createTime})")
    Boolean addUser(@Param("user")WechatUserBean user);

    @Update("UPDATE vaccine_wechat_user_tb SET login_code=#{user.loginCode},session_key=#{user.sessionKey} WHERE open_id=#{user.openId}")
    Boolean setUser(@Param("user")WechatUserBean user);

    @Select("SELECT * FROM vaccine_wechat_user_tb WHERE open_id=#{openId}")
    WechatUserBean getUserByOpenId(@Param("openId")String openId);

    @Select("SELECT * FROM vaccine_wechat_user_tb WHERE login_code=#{loginCode}")
    WechatUserBean getUserByLoginCode(@Param("loginCode")String loginCode);

    @Insert("INSERT INTO `vaccine_access_log`\n" +
            "(\n" +
            "`open_id`,\n" +
            "`user_agent`,\n" +
            "`user_ip`,\n" +
            "`service_category`,\n" +
            "`query_param`,\n" +
            "`access_fromid`,\n" +
            "`query_result_num`,\n" +
            "`access_date`)\n" +
            "VALUES\n" +
            "(\n" +
            "#{log.openId},\n" +
            "#{log.userAgent},\n" +
            "#{log.userIp},\n" +
            "#{log.serviceCategory},\n" +
            "#{log.queryParam},\n" +
            "#{log.accessFromid},\n" +
            "#{log.queryResultNum},\n" +
            "#{log.accessDate});\n" +
            "\n")
    Boolean addAccessLog(@Param("log")VaccineAccessLog log);

}

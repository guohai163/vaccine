package org.guohai.vaccine.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.guohai.vaccine.beans.UserQueryHistory;
import org.guohai.vaccine.beans.VaccineUserInfoBean;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VaccineUserDao {

    @Insert("INSERT INTO `vaccine_user_info_tb`\n" +
            "(`user_code`,`gender`,`avatar`,`city`,`country`,`nick_name`,`province`,`create_date`)\n" +
            "VALUES\n" +
            "(#{userInfo.userCode},#{userInfo.gender},\n" +
            "#{userInfo.avatar},#{userInfo.city},\n" +
            "#{userInfo.country},#{userInfo.nickName},\n" +
            "#{userInfo.province},#{userInfo.createDate});")
    Boolean addUserInfo(@Param("userInfo")VaccineUserInfoBean userInfo);

    @Select("SELECT * FROM vaccine_user_info_tb WHERE user_code=" +
            "(SELECT open_id FROM vaccine_wechat_user_tb WHERE login_code=#{loginCode})")
    VaccineUserInfoBean getUserInfo(@Param("loginCode") String loginCode);

    @Insert("INSERT INTO `vaccine_user_query_history_tb`\n" +
            "(`user_code`,`vaccine_code`,`query_date`)\n" +
            "VALUES\n" +
            "(#{userCode},#{vaccineCode},#{queryDate});")
    Boolean addQueryHistory(@Param("userCode") String userCode, @Param("vaccineCode") Integer vaccineCode,
                            @Param("queryDate") Date queryDate);

    @Select("SELECT query_date,vaccine_batch_tb.*,vaccine_url_tb.batch_url,vaccine_url_tb.batch_name FROM vaccine_user_query_history_tb \n" +
            "join vaccine_batch_tb on vaccine_user_query_history_tb.vaccine_code=vaccine_batch_tb.code\n" +
            "join vaccine_url_tb on vaccine_batch_tb.url_code=vaccine_url_tb.code\n" +
            "WHERE user_code=#{userCode}\n" +
            "ORDER BY query_date DESC limit 30;")
    List<UserQueryHistory> queryUserHistory(@Param("userCode") String userCode);

    @Select("select * from vaccine_user_info_tb WHERE user_code=#{userCode}")
    VaccineUserInfoBean getUserInfoByUserCode(@Param("userCode") String userCode);
}

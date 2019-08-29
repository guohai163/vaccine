package org.guohai.vaccine.dao;


import org.apache.ibatis.annotations.*;

public interface VaccineDao {

    @Insert("INSERT INTO `vaccine_url_tb`\n" +
            "(`batch_url`,`batch_name`)\n" +
            "VALUES\n" +
            "(#{batch_url},#{batch_name});\n")
    @Options(useGeneratedKeys = true, keyProperty = "code")
    Integer addBatchUrl(@Param("batch_url") String batchUrl, @Param("batch_name") String batchName, @Param("code") Integer code);

}

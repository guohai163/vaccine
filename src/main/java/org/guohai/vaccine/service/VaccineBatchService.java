package org.guohai.vaccine.service;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineBatchBean;

import java.util.List;

public interface VaccineBatchService {

    /**
     * nifdc数据处理
     * @return
     */
    Result<String> nifdcVaccineData(String yeah,String index);

    /**
     * 新版本nifdc数据处理
     * @return 返回处理结果
     */
    Result<String> newVersionNifdcVaccineData();

    /**
     *
     * @param battchNo
     * @param loginCode
     * @param formId
     * @param userIP
     * @param userAgent
     * @return
     */
    Result<List<VaccineBatchBean>> searchVaccineBatch(String battchNo, String loginCode, String formId, String userIP, String userAgent);

    /**
     * 获取最后更新时间
     * @return
     */
    Result<String> getLastDate();
}

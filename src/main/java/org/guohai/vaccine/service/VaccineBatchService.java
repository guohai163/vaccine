package org.guohai.vaccine.service;

import org.guohai.vaccine.org.guohai.vaccine.beans.Result;

public interface VaccineBatchService {

    /**
     * nifdc数据处理
     * @return
     */
    Result<String> nifdcVaccineData(String yeah,String index);
}

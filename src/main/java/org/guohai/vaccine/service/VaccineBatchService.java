package org.guohai.vaccine.service;

import org.guohai.vaccine.org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineBatchBean;

import java.util.List;

public interface VaccineBatchService {

    /**
     * nifdc数据处理
     * @return
     */
    Result<String> nifdcVaccineData(String yeah,String index);

    /**
     *
     * @param battchNo
     * @return
     */
    Result<List<VaccineBatchBean>> searchVaccineBatch(String battchNo);
}

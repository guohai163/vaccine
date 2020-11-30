package org.guohai.vaccine.utilities;


import org.guohai.vaccine.service.VaccineBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
public class CheckDataDailyUtilities {

    private static final Logger LOG  = LoggerFactory.getLogger(CheckDataDailyUtilities.class);

    @Autowired
    VaccineBatchService vaccineBatchService;

    @Scheduled(cron = "0 0 8 * * *")
    public void getNifdcData() {
        LOG.info("开始抓取数据，时间："+new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        vaccineBatchService.newVersionNifdcVaccineData();
//        if(c.get(Calendar.MONTH)==0){
//            vaccineBatchService.nifdcVaccineData(String.valueOf(c.get(Calendar.YEAR)-1),"");
//        }
//        vaccineBatchService.nifdcVaccineData(new SimpleDateFormat("yyyy").format( new Date()),"");
    }
}

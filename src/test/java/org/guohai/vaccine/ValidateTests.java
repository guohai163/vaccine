package org.guohai.vaccine;

import org.guohai.vaccine.service.VaccineBatchService;
import org.guohai.vaccine.utilities.VerificationUtilities;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidateTests {

    @Autowired
    private VaccineBatchService vaccineService;

    @Test
    public void validateString(){
        Assert.assertEquals(true,VerificationUtilities.valiadteHttpProtocol("https://www.nifdc.org.cn/nifdc/attach/swzp/20071216-20071231.htm"));
        Assert.assertEquals(true,VerificationUtilities.validateBatchNo("R0C471M"));
        Assert.assertEquals(true,VerificationUtilities.validateInteger("12"));
        Assert.assertEquals(true,VerificationUtilities.validateChineseDate("2019年12月1日"));
        Assert.assertEquals(true,VerificationUtilities.validateChineseDate("2019年12月"));
    }

//    @Test
    public void getPageData(){
        vaccineService.newVersionNifdcVaccineData();
    }
}

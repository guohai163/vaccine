package org.guohai.vaccine.controller;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.service.MiniProgramService;
import org.guohai.vaccine.service.VaccineBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/mini")
public class MiniProgramController {

    @Autowired
    MiniProgramService miniProgramService;

    @Autowired
    VaccineBatchService vaccineBatchService;

    @RequestMapping(value = "/oalogin")
    public Result<String> oalogin(@ModelAttribute("code") String code,@ModelAttribute("src") String src) {
        return miniProgramService.oalogin(code,src);
    }

    @ResponseBody
    @RequestMapping(value = "/search/{vackey}")
    public Result<List<VaccineBatchBean>> searchVaccine(@PathVariable("vackey") String vackey,
                                                        @ModelAttribute(value = "formId") String formId,
                                                        @RequestHeader(value = "login-code",required = false)String loginCode,
                                                        @RequestHeader(value = "X-Real-IP",required = false) String userIp,
                                                        @RequestHeader(value = "User-Agent", required =  false) String userAgent) {
        System.out.println(loginCode);
        vackey = vackey.replaceAll("%","");
        return vaccineBatchService.searchVaccineBatch(vackey, loginCode, formId,userIp , userAgent);
    }
}

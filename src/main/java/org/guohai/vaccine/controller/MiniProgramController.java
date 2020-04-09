package org.guohai.vaccine.controller;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.service.MiniProgramService;
import org.guohai.vaccine.service.VaccineBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/mini")
public class MiniProgramController {
    private static final Logger LOG  = LoggerFactory.getLogger(MiniProgramController.class);
    @Autowired
    MiniProgramService miniProgramService;

    @Autowired
    VaccineBatchService vaccineBatchService;

    @Autowired
    private HttpServletRequest request;

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
        vackey = vackey.replaceAll("%","");
        return vaccineBatchService.searchVaccineBatch(vackey, loginCode, formId,userIp , userAgent);
    }

    @ResponseBody
    @RequestMapping(value = "/getlast")
    @CrossOrigin
    public Result<String> getDataLast() {
        LOG.info("user in,ip:"+request.getRemoteAddr()+",UA:"+request.getHeader("User-Agent"));
        return vaccineBatchService.getLastDate();
    }


    @ResponseBody
    @RequestMapping(value = "/checkcode")
    public Result<String> checkLoginCode(@RequestHeader(value = "login-code",required = false)String loginCode){
        LOG.info(String.format("checklogin in:%s",loginCode));
        return miniProgramService.checkLoginCode(loginCode);
    }

}

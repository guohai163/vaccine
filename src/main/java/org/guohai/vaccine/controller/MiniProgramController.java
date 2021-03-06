package org.guohai.vaccine.controller;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.UserQueryHistory;
import org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.beans.VaccineUserInfoBean;
import org.guohai.vaccine.service.MiniProgramService;
import org.guohai.vaccine.service.VaccineBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/ali_login")
    public Result<String> aliminiLogin(@ModelAttribute("code") String code) {
        return miniProgramService.aliMiniLogin(code);
    }

    @RequestMapping(value = "/oalogin")
    public Result<String> oalogin(@ModelAttribute("code") String code, @ModelAttribute("src") String src,
                                  @ModelAttribute("share_data") String shareData, @ModelAttribute("iv") String iv) {
        return miniProgramService.oalogin(code, src, shareData, iv);
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
//        LOG.info("user in,ip:"+request.getRemoteAddr()+",UA:"+request.getHeader("User-Agent"));
        return vaccineBatchService.getLastDate();
    }


    @ResponseBody
    @RequestMapping(value = "/checkcode")
    public Result<String> checkLoginCode(@RequestHeader(value = "login-code",required = false)String loginCode){
        LOG.info(String.format("checklogin in:%s",loginCode));
        return miniProgramService.checkLoginCode(loginCode);
    }


    /***
     * 提交查询结果值
     * @param loginCode
     * @param jsonParam
     * @return
     */
    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value = "/postvcode", produces = "application/json;charset=UTF-8")
    @PostMapping(value = "/postvcode")
    public Result<String> saveQueryResult(@RequestHeader(value = "login-code",required = false)String loginCode,
                                          @RequestBody Map<String, Object> jsonParam){
        LOG.info(String.format("parm %s %s", loginCode, jsonParam.get("vaccineCode")));
        return miniProgramService.addUserQueryHistory(loginCode, Integer.parseInt(jsonParam.get("vaccineCode").toString()));
    }

    @ResponseBody
    @GetMapping(value = "/getmylist")
    public Result<List<UserQueryHistory>> getMySaveList(@RequestHeader(value = "login-code",required = false)String loginCode) {
        return miniProgramService.queryUserHistory(loginCode);
    }

    @ResponseBody
    @GetMapping(value = "/getuserinfo")
    public Result<VaccineUserInfoBean> getUserInfo(@RequestHeader(value = "login-code",required = false)String loginCode) {
        return miniProgramService.getUserInfo(loginCode);
    }

    @ResponseBody
    @PostMapping(value = "/postuserinfo")
    public Result<String> postUserInfo(@RequestHeader(value = "login-code",required = false)String loginCode,
                                       @RequestBody Map<String, Object> jsonParam) {
        VaccineUserInfoBean userInfo = new VaccineUserInfoBean("", jsonParam.get("gender").toString(),
                jsonParam.get("avatar").toString(), jsonParam.get("city").toString(), jsonParam.get("countryCode").toString(),
                jsonParam.get("nickName").toString(), jsonParam.get("province").toString(), new Date());
        return miniProgramService.addUserInfo(loginCode, userInfo);
    }
}

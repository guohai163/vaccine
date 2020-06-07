package org.guohai.vaccine.controller;

import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.WechatUserInfoBean;
import org.guohai.vaccine.service.AdminService;
import org.guohai.vaccine.service.MiniProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 管理后台 ，所有功能需要登录
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    MiniProgramService miniProgramService;

    @Autowired
    AdminService adminSerivce;

    @RequestMapping("/log")
    public String getAccessLogs(Model model) {
        model.addAttribute("accesslogs",miniProgramService.getAccessLogs());
        return "logs";
    }

    @ResponseBody
    @RequestMapping(value = "/wxacode/{src}")
    public ResponseEntity<byte[]> getWxaCode(@PathVariable("src") String src) {
        if(src == null || src.length()==0){
            return new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentDispositionFormData(String.format("%s.jpeg",src),null);
        return  new ResponseEntity<>(miniProgramService.getWcaCode(src),headers,HttpStatus.OK);
    }

    @RequestMapping("/userlist")
    public String getUserList(Model model) {
        model.addAttribute("userlist",miniProgramService.getUserList());
        return "userlist";
    }

    @ResponseBody
    @RequestMapping(value = "/userinfo", method = RequestMethod.PUT)
    public Result<String> putUserMoreInfo(@RequestBody WechatUserInfoBean userInfo) {

        return miniProgramService.putUserMoreInfo(userInfo);
    }
    @RequestMapping("/userinfo")
    public String getUserInfo(Model model, @ModelAttribute(value = "usercode")String userCode) {
        model.addAttribute("userinfo", adminSerivce.getUserInfoByCode(userCode));
        return "admin/userinfo";
    }
}

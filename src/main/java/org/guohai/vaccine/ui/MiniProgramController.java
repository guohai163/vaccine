package org.guohai.vaccine.ui;

import org.guohai.vaccine.org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.service.MiniProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mini")
public class MiniProgramController {

    @Autowired
    MiniProgramService miniProgramService;

    @RequestMapping(value = "/oalogin")
    public Result<String> oalogin(@ModelAttribute("code") String code) {
        return miniProgramService.oalogin(code);
    }

}

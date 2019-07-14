package org.guohai.vaccine.ui;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @RequestMapping(value = "/")
    public String  index() {
        return "aa";
    }
}

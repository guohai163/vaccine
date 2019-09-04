package org.guohai.vaccine.controller;

import org.guohai.vaccine.beans.BindDataToBean;
import org.guohai.vaccine.beans.Result;
import org.guohai.vaccine.beans.VaccineBatchBean;
import org.guohai.vaccine.beans.VaccineDateBean;
import org.guohai.vaccine.service.VaccineBatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 程序主Controller负责展示和计算
 */
@Controller
class CalculatorController {
    private static final Logger LOG  = LoggerFactory.getLogger(CalculatorController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BindDataToBean vaccineData;

    @Autowired
    private VaccineBatchService vaccineBatchService;

    @ResponseBody
    @RequestMapping(value = "/catch/{yeah}")
    public Result<String> catchData(@PathVariable("yeah") String yeah,@ModelAttribute("index") String index) {
        return  vaccineBatchService.nifdcVaccineData(yeah,index);
    }

    @ResponseBody
    @RequestMapping(value = "/search/{vackey}")
    public Result<List<VaccineBatchBean>> searchVaccine(@PathVariable("vackey") String vackey) {
        vackey = vackey.replaceAll("%","");
        return vaccineBatchService.searchVaccineBatch(vackey);
    }

    @ResponseBody
    @RequestMapping(value = "/getlast")
    public Result<String> getDataLast() {
        LOG.info("user in,ip:"+request.getRemoteAddr()+",UA:"+request.getHeader("User-Agent"));
        return vaccineBatchService.getLastDate();
    }
    /**
     * 首页Action
     * @param model 接收页面传入
     * @return 返回模板名称
     */
    @RequestMapping(value = "/")
    public String  index(Model model) {

        model.addAttribute("mapVaccine", vaccineData.getMapVaccine());

        return "index";
    }

    @RequestMapping(value = "/result")
    public String result(Model model,String brdate,String[] level2vaccine) {
        model.addAttribute("brdate",brdate);

        if(level2vaccine == null) {

            model.addAttribute("mapVaccine", vaccineData.getMapVaccine());
            return "result";
        }
        HashMap<String,VaccineDateBean> mapVaccine = copy(vaccineData.getMapVaccine());
        for(String l2:level2vaccine) {
            switch (l2) {
                case "DTaP-IPV/Hib":
                    mapVaccine.get("IPV").setState(false);
                    mapVaccine.get("OPV").setState(false);
                    mapVaccine.get("DTaP").setState(false);
                    mapVaccine.get("DTaP-IPVHib").setState(true);
                    break;
                case "PCV13":
                    mapVaccine.get("PCV13").setState(true);
                    break;
                case "LLR":
                    mapVaccine.get("LLR").setState(true);
                    break;

            }
        }

        model.addAttribute("mapVaccine", mapVaccine);
        return "result";
    }

    /**
     * COPY类的一个简单防范，高度定制版本
     * @param original 输入待深拷贝的对象
     * @return 返回深度拷贝结果
     */
    private HashMap<String,VaccineDateBean> copy(HashMap<String, VaccineDateBean> original) {
        HashMap<String, VaccineDateBean> copy = new HashMap<>();

        for(Map.Entry<String,VaccineDateBean> entry: original.entrySet()){
            VaccineDateBean val = new VaccineDateBean();
            val.setKeyName(entry.getValue().getKeyName());
            val.setDes(entry.getValue().getDes());
            val.setState(entry.getValue().isState());
            val.setAccinationMonthAge(entry.getValue().getAccinationMonthAge());
            copy.put(entry.getKey(),val);
        }
        return copy;
    }



}

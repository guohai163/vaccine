package org.guohai.vaccine.ui;

import org.guohai.vaccine.org.guohai.vaccine.beans.BindDataToBean;
import org.guohai.vaccine.org.guohai.vaccine.beans.VaccineDateBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.HashMap;

@Controller
public class CalculatorController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BindDataToBean vaccineData;

    private HashMap<String , VaccineDateBean> vmap = new HashMap<>();

    @RequestMapping(value = "/")
    public String  index(Model model) {

        model.addAttribute("user",getIpAddr(request));
        return "index";
    }

    @RequestMapping(value = "/result")
    public String result(Model model,String brdate,String level2vaccine) {


//        vaccineData.getMapVaccine().get("IPV").setState(false);

        model.addAttribute("brdate",brdate);
        model.addAttribute("mapVaccine", vaccineData.getMapVaccine());
        return "result";
    }

    private void initVaccine() {

    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;

                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

}

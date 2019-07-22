package org.guohai.vaccine.org.guohai.vaccine.beans;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
@ConfigurationProperties(prefix = "my-data")
public class BindDataToBean {

    private HashMap<String, VaccineDateBean> mapVaccine = new HashMap<>();

    public HashMap<String , VaccineDateBean> getMapVaccine() {
        return mapVaccine;
    }

    public void setMapVaccine(HashMap<String, VaccineDateBean> mapVaccine) {
        this.mapVaccine = mapVaccine;
    }

}

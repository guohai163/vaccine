package org.guohai.vaccine.org.guohai.vaccine.beans;

import lombok.Getter;
import lombok.Setter;

public class VaccineDateBean {

    @Getter
    @Setter
    private String keyName;

    @Getter
    @Setter
    private  String des;

    @Getter
    @Setter
    private Integer[] accinationMonthAge;

    @Getter
    @Setter
    private boolean state;
}

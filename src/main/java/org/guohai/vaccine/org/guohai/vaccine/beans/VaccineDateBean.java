package org.guohai.vaccine.org.guohai.vaccine.beans;

import lombok.Getter;
import lombok.Setter;

/**
 * 疫苗实体
 */
public class VaccineDateBean {

    /**
     * 疫苗英文缩写
     */
    @Getter
    @Setter
    private String keyName;

    /**
     * 疫苗中文名字
     */
    @Getter
    @Setter
    private  String des;

    /**
     * 疫苗接种时间数组，
     */
    @Getter
    @Setter
    private Integer[] accinationMonthAge;

    /**
     * 是否显示状态位
     */
    @Getter
    @Setter
    private boolean state;

}

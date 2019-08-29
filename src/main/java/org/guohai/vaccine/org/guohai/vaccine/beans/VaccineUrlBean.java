package org.guohai.vaccine.org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineUrlBean {

    private Integer code;

    private String batchUrl;

    private String batchName;
}

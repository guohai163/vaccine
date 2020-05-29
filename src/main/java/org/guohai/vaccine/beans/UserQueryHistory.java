package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserQueryHistory extends VaccineBatchBean {
    private Date queryDate;
    private String sQueryDate;
}

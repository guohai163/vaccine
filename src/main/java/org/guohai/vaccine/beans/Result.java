package org.guohai.vaccine.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Boolean status;

    private T data;

    public Result(boolean b, T no_login_code) {
    }
}

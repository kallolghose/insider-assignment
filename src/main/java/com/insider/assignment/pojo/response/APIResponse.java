package com.insider.assignment.pojo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class APIResponse<T> {

    public Boolean status;
    private String message;
    private List<String> errors;
    private T data;

}

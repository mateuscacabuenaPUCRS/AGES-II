package com.ensportive.exceptions;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ErrorDTO {

    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("time_stamp")
    private Date timestamp;
}

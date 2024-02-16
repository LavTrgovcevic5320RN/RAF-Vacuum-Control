package com.example.demo.responses;

import lombok.Data;

@Data
public class OperationMessage {
    private String msg;

    public OperationMessage(String msg) {
        this.msg = msg;
    }
}

package com.project.manager.constant;

import lombok.Getter;

@Getter
public enum StatusEnum {

    OPEN("O"), FINISHED("F");

    private final String code;

    private StatusEnum(String code) {
        this.code = code;
    }
}

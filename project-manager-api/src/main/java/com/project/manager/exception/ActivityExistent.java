package com.project.manager.exception;

public class ActivityExistent extends RuntimeException {

    public ActivityExistent() {
        super();
    }

    public ActivityExistent(String message) {
        super(message);
    }
}

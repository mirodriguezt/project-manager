package com.project.manager.exception;

public class ActivityNonExistent extends RuntimeException {

    public ActivityNonExistent() {
        super();
    }

    public ActivityNonExistent(String message) {
        super(message);
    }
}

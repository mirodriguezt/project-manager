package com.project.manager.exception;

public class ProjectExistent extends RuntimeException {

    public ProjectExistent() {
        super();
    }

    public ProjectExistent(String message) {
        super(message);
    }
}

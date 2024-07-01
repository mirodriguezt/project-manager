package com.project.manager.exception;

public class ProjectNonExistent extends RuntimeException {

    public ProjectNonExistent() {
        super();
    }

    public ProjectNonExistent(String message) {
        super(message);
    }
}

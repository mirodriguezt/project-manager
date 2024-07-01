package com.project.manager.exception;

public class ClientNonExistent extends RuntimeException {

    public ClientNonExistent() {
        super();
    }

    public ClientNonExistent(String message) {
        super(message);
    }
}

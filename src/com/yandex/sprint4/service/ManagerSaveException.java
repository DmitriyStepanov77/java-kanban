package com.yandex.sprint4.service;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final Exception e) {
        super(e);
    }
}

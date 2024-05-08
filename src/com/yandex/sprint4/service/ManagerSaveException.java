package com.yandex.sprint4.service;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final Exception e) {
        super(e);
    }
}

package com.yandex.sprint4.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void getTaskManager() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

        assertNotNull(inMemoryTaskManager);
    }

    @Test
    public void getHistoryManager() {
        InMemoryHistoryManager inMemoryHistoryManager = (InMemoryHistoryManager) Managers.getHistory();

        assertNotNull(inMemoryHistoryManager);
    }

}
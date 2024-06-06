package com.yandex.sprint4.service;

import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        super.taskManager = Managers.getDefault();
    }

}
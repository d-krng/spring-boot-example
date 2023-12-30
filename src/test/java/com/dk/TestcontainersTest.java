package com.dk;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestcontainersTest extends AbstractTestcontainers {

    @Test
    void canStartPostgresDB() {
        //Then
        Assertions.assertThat(postgreSQLContainer.isRunning()).isTrue();
        Assertions.assertThat(postgreSQLContainer.isCreated()).isTrue();
    }


}

package com.github.wendt.ifood;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CadastroTestLifecycleManager implements QuarkusTestResourceLifecycleManager {
    //    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:12.2");
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

    @Override
    public Map<String, String> start() {
        POSTGRES.start();
        Map<String, String> propriedades = new HashMap<String, String>();

        //Variáveis de acesso ao banco de dados.
        propriedades.put("quarkus.datasource.jdbc.url", POSTGRES.getJdbcUrl());
        propriedades.put("quarkus.datasource.username", POSTGRES.getUsername());
        propriedades.put("quarkus.datasource.password", POSTGRES.getPassword());
        return propriedades;
    }

    @Override
    public void stop() {
        if (POSTGRES != null && POSTGRES.isRunning())
            POSTGRES.stop();
    }
}

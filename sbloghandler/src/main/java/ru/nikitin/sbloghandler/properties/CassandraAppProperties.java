package ru.nikitin.sbloghandler.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Properties приложения для работы с кассанрдрой
 * */
@Component
@Data
public class CassandraAppProperties {
    @Value("${cassandra.connection-host}")
    private String connectionHost;
    @Value("${cassandra.key-space}")
    private String keySpace;
    @Value("${cassandra.table}")
    private String table;
    @Value("${cassandra.port}")
    private String port;
}

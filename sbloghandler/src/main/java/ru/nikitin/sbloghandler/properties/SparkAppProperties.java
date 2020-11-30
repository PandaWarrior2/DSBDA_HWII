package ru.nikitin.sbloghandler.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Properties приложения для работы со спарком
 * */
@Component
@Data
public class SparkAppProperties {
    @Value("${spark.streaming-duration}")
    private String streamingDuration;
    @Value("${spark.master}")
    private String master;
    @Value("${spark.executor-memory}")
    private String executorMemory;
    @Value("${spark.driver-memory}")
    private String driverMemory;
    @Value("${spark.driver-max-result-size}")
    private String driverMaxResultSize;
    @Value("${spark.app-name}")
    private String appName;
}

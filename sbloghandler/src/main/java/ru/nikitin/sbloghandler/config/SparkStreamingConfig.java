package ru.nikitin.sbloghandler.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nikitin.sbloghandler.dto.LogDTO;
import ru.nikitin.sbloghandler.properties.CassandraAppProperties;
import ru.nikitin.sbloghandler.properties.KafkaAppProperties;
import ru.nikitin.sbloghandler.properties.SparkAppProperties;

import java.util.*;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

/**
 * Сервис слушающий раз в N секунд сообщения в кафка и отсылающий их в кассандру
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SparkStreamingConfig {
    private final SparkAppProperties sparkProperties;
    private final KafkaAppProperties kafkaProperties;
    private final CassandraAppProperties cassandraProperties;
    private final ObjectMapper objectMapper;

    /**
     * Метод пересылки сообщений из кафки в кассандру (вызывается при старте приложения)
     */
    @Bean
    public JavaStreamingContext javaStreamingContext(SparkConf sparkConf) {
        JavaSparkContext sc = new JavaSparkContext(SparkContext.getOrCreate(sparkConf));
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sc, Durations.seconds(Long.parseLong(sparkProperties.getStreamingDuration())));
        Collection<String> topics = Collections.singletonList(kafkaProperties.getTopic());
        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils
                .createDirectStream(
                        javaStreamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, consumerProperties())
                );
        JavaDStream<String> lines = messages.map(ConsumerRecord::value);
        lines.foreachRDD(
                stringJavaRDD -> {
                    List<String> strings = stringJavaRDD.collect();
                    if (!strings.isEmpty()) {
                        List<LogDTO> logDTOs = new ArrayList<>();
                        strings.forEach(s -> {
                            try {
                                LogDTO logDTO = objectMapper.readValue(s, LogDTO.class);
                                logDTOs.add(logDTO);
                            } catch (JsonProcessingException jsonMappingException) {
                                log.info("Some incorrect LogDTO");
                            }
                        });
                        JavaRDD<LogDTO> logsRdd = javaStreamingContext.sparkContext().parallelize(logDTOs);
                        javaFunctions(logsRdd)
                                .writerBuilder(
                                        cassandraProperties.getKeySpace(),
                                        cassandraProperties.getTable(),
                                        mapToRow(LogDTO.class)
                                ).saveToCassandra();
                    }

                });
        return javaStreamingContext;
    }

    /**
     * Метод выдачи конифгурации кафка-продьюсера
     * */
    private Map<String, Object> consumerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroupId());
        return properties;
    }
}

package ru.nikitin.sbloghandler.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Сервис слушающий раз в N секунд сообщения в кафка и отсылающий их в кассандру*/
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListener {

    private final JavaStreamingContext javaStreamingContext;

    /**
     * Метод пересылки сообщений из кафки в кассандру (вызывается при старте приложения)
     * */
    @EventListener(ApplicationStartedEvent.class)
    public void startKafkaStreamers() throws InterruptedException {
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}

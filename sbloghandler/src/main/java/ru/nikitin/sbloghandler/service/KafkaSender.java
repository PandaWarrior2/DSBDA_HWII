package ru.nikitin.sbloghandler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.nikitin.sbloghandler.dto.LogDTO;
import ru.nikitin.sbloghandler.properties.KafkaAppProperties;

import java.io.File;
import java.util.List;


/**
 * Сервис отправки логов в кафку*/
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

    private final LogParser csvService;

    private final KafkaAppProperties kafkaProperties;

    private final KafkaTemplate<Long, LogDTO> kafkaTemplate;

    /**
     * Метод отправки логов из входного файла в кафку
     * @param messageFile - путь до входного файла */
    public List<LogDTO> sendLogs(String messageFile) {
        File logFile = new File(messageFile);
        List<LogDTO> rowList = csvService.parseLog(logFile);
        rowList.forEach(this::send);
        return rowList;
    }

    /**
     * Метод отправки строки с логом в кафку
     * @param row - строка*/
    private void send(LogDTO row) {
        log.info("sending testDataDto='{}'", row.toString());
        kafkaTemplate.send(kafkaProperties.getTopic(), row);
    }
}
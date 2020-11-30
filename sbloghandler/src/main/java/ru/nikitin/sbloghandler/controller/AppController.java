package ru.nikitin.sbloghandler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nikitin.sbloghandler.dto.ResultData;
import ru.nikitin.sbloghandler.service.KafkaSender;
import ru.nikitin.sbloghandler.service.StatisticService;

import java.util.List;

/**
 * Web-контроллер приложения */
@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class AppController {

    private final KafkaSender kafkaSender;
    private final StatisticService statisticService;

    /**
     * Эндпоинт отправки логов в кафку,
     * @param filePath путь до отправляемого файла с логами */
    @PostMapping("/send")
    public HttpStatus sendLogs(@RequestParam String filePath) {
        kafkaSender.sendLogs(filePath);
        return HttpStatus.OK;
    }

    /**
     * Эндпоинт подсчёта выходных данных*/
    @GetMapping("/compute")
    public ResponseEntity<List<ResultData>> compute() {
        return ResponseEntity.ok(statisticService.compute());
    }
}

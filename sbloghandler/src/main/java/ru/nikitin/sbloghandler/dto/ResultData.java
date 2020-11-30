package ru.nikitin.sbloghandler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * POJO выходных данных (результата)*/
@Data
@AllArgsConstructor
public class ResultData implements Serializable {
    private Integer hour;
    private Integer priority;
    private Integer count;
}

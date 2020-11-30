package ru.nikitin.sbloghandler.service;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.stereotype.Service;
import ru.nikitin.sbloghandler.dto.LogDTO;
import ru.nikitin.sbloghandler.dto.ResultData;
import ru.nikitin.sbloghandler.properties.CassandraAppProperties;
import scala.Tuple2;

import java.util.Calendar;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;

/**
 * Сервис подсчёта статистики по логам
 * */
@Service
public class StatisticService {
    private final JavaSparkContext javaSparkContext;
    private final CassandraAppProperties cassandraProperties;

    public StatisticService(SparkConf sparkConf, CassandraAppProperties cassandraProperties) {
        this.javaSparkContext = new JavaSparkContext(SparkContext.getOrCreate(sparkConf));
        this.cassandraProperties = cassandraProperties;
    }

    /**
     * Метод подсчёта статистика с помощью spark RDD*/
    public List<ResultData> compute() {
        Calendar calendar = Calendar.getInstance();

        return javaFunctions(javaSparkContext)
                .cassandraTable(
                        cassandraProperties.getKeySpace(),
                        cassandraProperties.getTable(),
                        CassandraJavaUtil.mapRowTo(LogDTO.class)
                )
                .mapToPair(logDTO -> {
                    calendar.setTimeInMillis(logDTO.getDatetime().getTime());
                    return new Tuple2<>(new Tuple2<Integer, Integer>(calendar.get(Calendar.HOUR_OF_DAY), logDTO.getPriority()), 1);
                })
                .reduceByKey(Integer::sum)
                .map(tuple2IntegerTuple2 -> new ResultData(tuple2IntegerTuple2._1._1, tuple2IntegerTuple2._1._2, tuple2IntegerTuple2._2))
                .sortBy(ResultData::getPriority, false, 10)
                .sortBy(ResultData::getHour, true, 10)
                .collect();
    }
}

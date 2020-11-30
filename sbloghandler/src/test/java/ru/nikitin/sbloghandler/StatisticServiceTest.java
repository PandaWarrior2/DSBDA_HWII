package ru.nikitin.sbloghandler;

import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nikitin.sbloghandler.config.SparkAppConfig;
import ru.nikitin.sbloghandler.properties.CassandraAppProperties;
import ru.nikitin.sbloghandler.properties.SparkAppProperties;
import ru.nikitin.sbloghandler.service.StatisticService;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static ru.nikitin.sbloghandler.utils.TestUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        CassandraAppProperties.class,
        SparkAppProperties.class,
        SparkAppConfig.class,
        StatisticService.class
})
@TestPropertySource(properties = {
        "cassandra.connection-host=localhost",
        "cassandra.key-space=linux_logs",
        "cassandra.table=logs",
        "cassandra.port=9142",
        "spark.streaming-duration=1",
        "spark.master=local[2]",
        "spark.executor-memory=1g",
        "spark.driver-memory=1g",
        "spark.driver-max-result-size=1g",
        "spark.app-name=kafka-cassandra-spring-test"
})
public class StatisticServiceTest {

    @Autowired
    private StatisticService statisticService;

    @Rule
    public CassandraCQLUnit cassandraCQLUnit =
            new CassandraCQLUnit(new ClassPathCQLDataSet(
                    "dataset.cql", false, false));

    @After
    public void cleanUp() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

    @Test
    public void testCompute() {
        assertEquals(expectedStatisticData, new HashSet<>(statisticService.compute()));
    }

}
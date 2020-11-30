package ru.nikitin.sbloghandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nikitin.sbloghandler.dto.LogDTO;
import ru.nikitin.sbloghandler.service.LogParser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static ru.nikitin.sbloghandler.utils.TestUtils.expectedParsedData;

@ContextConfiguration(classes = LogParser.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LogParserTest {

    @Autowired
    private LogParser logParser;

    @Test
    public void testLogParser() {
        List<LogDTO> actualData = logParser.parseLog(new File("src/test/resources/message.log"));
        assertEquals(
                expectedParsedData.stream().map(LogDTO::getDatetime).collect(Collectors.toList()),
                actualData.stream().map(LogDTO::getDatetime).collect(Collectors.toList())
        );
        assertEquals(
                expectedParsedData.stream().map(LogDTO::getHostname).collect(Collectors.toList()),
                actualData.stream().map(LogDTO::getHostname).collect(Collectors.toList())
        );
        assertEquals(
                expectedParsedData.stream().map(LogDTO::getMessage).collect(Collectors.toList()),
                actualData.stream().map(LogDTO::getMessage).collect(Collectors.toList())
        );
        assertEquals(
                expectedParsedData.stream().map(LogDTO::getProcess).collect(Collectors.toList()),
                actualData.stream().map(LogDTO::getProcess).collect(Collectors.toList())
        );
        assertEquals(
                expectedParsedData.stream().map(LogDTO::getPriority).collect(Collectors.toList()),
                actualData.stream().map(LogDTO::getPriority).collect(Collectors.toList())
        );
    }
}

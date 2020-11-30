package ru.nikitin.sbloghandler.utils;

import ru.nikitin.sbloghandler.dto.LogDTO;
import ru.nikitin.sbloghandler.dto.ResultData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtils {
    public static final Set<ResultData> expectedStatisticData = new HashSet<ResultData>() {{
        add(new ResultData(0, 0, 1));
        add(new ResultData(0, 1, 1));
        add(new ResultData(1, 1, 2));
        add(new ResultData(2, 2, 2));
        add(new ResultData(2, 3, 1));
        add(new ResultData(3, 3, 1));
        add(new ResultData(4, 4, 1));
        add(new ResultData(4, 5, 2));
        add(new ResultData(5, 6, 1));
    }};

    public static final List<LogDTO> expectedParsedData = new ArrayList<LogDTO>() {{
        add(new LogDTO(1L, Timestamp.valueOf("2020-11-20 00:00:00"), "localhost1", "root1[11111]", "dummy message1 emerg", 0 ));
        add(new LogDTO(2L, Timestamp.valueOf("2020-11-20 00:00:00"), "localhost1", "root2[11111]", "dummy message2 alert", 1 ));
        add(new LogDTO(3L, Timestamp.valueOf("2020-11-20 01:00:00"), "localhost2", "root3[11111]", "dummy message1 alert", 1 ));
        add(new LogDTO(4L, Timestamp.valueOf("2020-11-20 01:00:00"), "localhost2", "root4[11111]", "dummy message2 alert", 1 ));
        add(new LogDTO(5L, Timestamp.valueOf("2020-11-20 02:00:00"), "localhost3", "root5[11111]", "dummy message1 crit", 2 ));
        add(new LogDTO(6L, Timestamp.valueOf("2020-11-20 02:00:00"), "localhost3", "root6[11111]", "dummy message2 error", 3 ));
        add(new LogDTO(7L, Timestamp.valueOf("2020-11-20 02:00:00"), "localhost4", "root6[11111]", "dummy message1 crit", 2 ));
        add(new LogDTO(8L, Timestamp.valueOf("2020-11-20 03:00:00"), "localhost4", "root5[11111]", "dummy message2 error", 3 ));
        add(new LogDTO(9L, Timestamp.valueOf("2020-11-20 04:00:00"), "localhost5", "root4[11111]", "dummy message1 warning", 4 ));
        add(new LogDTO(10L, Timestamp.valueOf("2020-11-20 04:00:00"), "localhost5", "root3[11111]", "dummy message2 notice", 5 ));
        add(new LogDTO(11L, Timestamp.valueOf("2020-11-20 04:00:00"), "localhost6", "root2[11111]", "dummy message1 notice", 5 ));
        add(new LogDTO(12L, Timestamp.valueOf("2020-11-20 05:00:00"), "localhost6", "root1[11111]", "dummy message2 info", 6 ));
    }};
}

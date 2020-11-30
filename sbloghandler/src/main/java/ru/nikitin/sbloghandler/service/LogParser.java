package ru.nikitin.sbloghandler.service;

import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nikitin.sbloghandler.dto.LogDTO;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogParser {

    private static final Logger logger = LoggerFactory.getLogger(LogParser.class);

    /**
     * Parse system log
     * @param log File must be parsed
     * @return List of metrics
     */
    public List<LogDTO> parseLog(File log){
        List<LogDTO> rowList = new ArrayList<>();

        Map<Integer, List<String>> priorities = new HashMap<Integer, List<String>>() {{
            put(0, Arrays.asList("emerg", "panic"));
            put(1, Arrays.asList("alert"));
            put(2, Arrays.asList("crit"));
            put(3, Arrays.asList("error", "err"));
            put(4, Arrays.asList("warning", "warn"));
            put(5, Arrays.asList("notice"));
            put(6, Arrays.asList("info"));
            put(7, Arrays.asList("debug"));
        }};
        try{
            Scanner scan = new Scanner(log);
            scan.useDelimiter("\n");
            do{
                Pattern pat = Pattern.compile("([A-Za-z]{3}\\s[\\d]{2}\\s\\d{2}:\\d{2}:\\d{2})\\s([\\S]*)\\s(\\S*):\\s(.*)", Pattern.MULTILINE);
                String row = scan.next();
                Matcher matcher = pat.matcher(row);
                while(matcher.find()){
                    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss", Locale.US);
                    Date dt = formatter.parse(matcher.group(1));
                    dt.setYear(new Date(System.currentTimeMillis()).getYear());
                    Timestamp timestamp = new Timestamp(dt.getTime());
                    String hostname = matcher.group(2);
                    String process = matcher.group(3);
                    String message = matcher.group(4);
                    Integer priority = 8;
                    for(Map.Entry<Integer, List<String>> map_i: priorities.entrySet()){
                        boolean match = map_i.getValue().stream().anyMatch(message::contains);
                        if(match){
                            priority = map_i.getKey();
                            break;
                        }
                    }
                    long id = UUIDs.timeBased().timestamp();
                    LogDTO dto = new LogDTO(id, timestamp, hostname, process, message, priority);
                    rowList.add(dto);
                }
            }while(scan.hasNext());
            scan.close();
        }
        catch (IOException | ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return rowList;
    }
}

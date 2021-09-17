package utils;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimestampParser {

    public static Timestamp parse(String stringTimestamp) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = dateFormat.parse(stringTimestamp);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        return timestamp;
    }
}

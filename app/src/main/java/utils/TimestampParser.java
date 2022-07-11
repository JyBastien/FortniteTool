package utils;

import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/*Classe qui prend en charge la transformation d'un String timestamp et retourne un
* objet Timestamp*/
public class TimestampParser {

    public static Timestamp parse(String stringTimestamp) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = dateFormat.parse(stringTimestamp);
        return new Timestamp(parsedDate.getTime());

    }
}

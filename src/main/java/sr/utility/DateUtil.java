package sr.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getNowDateForFileName(){
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
    }
    public static String getNowDateForlogs(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}

package utility;

public class StringUtil {
    public static boolean isEmpty(String value){
        if(value==null || value.equals("")){
            return true;
        }
        return false;
    }
}

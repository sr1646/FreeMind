package sr.utility;

public class StringUtil {
    public static boolean isEmpty(String value){
        if(value==null || value.equals("")){
            return true;
        }
        return false;
    }
    public static boolean isNotEmpty(String value){
        return (!isEmpty(value));
    }
    public static String chkNull(Object str){
        if(str==null){
            return "";
        }
        return String.valueOf(str).trim();
    }
}

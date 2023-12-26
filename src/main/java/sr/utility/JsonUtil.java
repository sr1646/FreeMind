package sr.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonUtil {
    public static List<DVDHelper> getDVDList(String dvdDataJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<DVDHelper> dvdList=null;
        try {
            dvdList = Arrays.asList(mapper.readValue(dvdDataJson,DVDHelper[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dvdList;
    }
    public static String objectToJSON(Object o){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static  List<File> getFileList(String fileListInJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<File> file=null;
        try {
            file = Arrays.asList(mapper.readValue(fileListInJson,File[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return file;
    }
    public static Set getSetFromJSon(String setJson) {
        ObjectMapper mapper = new ObjectMapper();
        Set set=null;
        try {
            set=mapper.readValue(setJson, HashSet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return set;
    }
    public static List getListFromJSon(String setJson) {
        ObjectMapper mapper = new ObjectMapper();
        List set=null;
        try {
            set=mapper.readValue(setJson, ArrayList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return set;
    }
}

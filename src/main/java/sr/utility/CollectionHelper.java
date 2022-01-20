package sr.utility;

import java.util.*;
import java.util.Map.Entry;

public class CollectionHelper {
    /*
    Solution: The idea is to store the entry set in a list and sort the list on the basis of values. Then fetch values and keys from the list and put them in a new hashmap. Thus, a new hashmap is sorted according to values.
Below is the implementation of the above idea:
     */
    public static <K,V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map){
        List<Entry<K,V>> entryList=new ArrayList(map.entrySet());
        entryList.sort(Entry.comparingByValue());

        Map<K,V> sortedMap=new LinkedHashMap<>();
        for (Entry<K, V> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    public static <K extends Comparable<? super K> ,V> Map<K, V> sortByKey(Map<K, V> map){
        List<Entry<K,V>> entryList=new ArrayList(map.entrySet());
        entryList.sort(Entry.comparingByKey());

        Map<K,V> sortedMap=new LinkedHashMap<>();
        for (Entry<K, V> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    public  static boolean  isEmpty(List list){
        if(list==null){
            return true;
        }else if(list.isEmpty()){
            return true;
        }
        return false;
    }
    public  static boolean  isNotEmpty(List list){
        return (!isEmpty(list));
    }

    public  static boolean  isEmpty(Set set){
        if(set==null){
            return true;
        }else if(set.isEmpty()){
            return true;
        }
        return false;
    }
    public  static boolean  isNotEmpty(Set set){
        return (!isEmpty(set));
    }
    public  static boolean  isEmpty(Map map){
        if(map==null){
            return true;
        }else if(map.isEmpty()){
            return true;
        }
        return false;
    }
    public  static boolean  isNotEmpty(Map map){
        return (!isEmpty(map));
    }
}

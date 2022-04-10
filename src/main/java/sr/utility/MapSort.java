package sr.utility;

import java.util.*;

import static sr.utility.Output.debug;

public class MapSort<K,V> {
    public  Map<K,V> sortMapByValue(Map<K,V> map){
        List<Map.Entry<K,V>> dataList=new ArrayList<>(map.entrySet());

        Collections.sort(dataList, new Comparator<Map.Entry<K,V>>() {
            @Override
            public int compare(Map.Entry<K,V> o1, Map.Entry<K,V> o2) {
                if(o1.getValue().getClass()!=o2.getValue().getClass()){
                    try {
                        throw new Exception("Two object class not matching: "+o1.getClass()+" != "+o2.getClass());
                    } catch (Exception e) {
                        Output.exception(e);
                        debug(e.getMessage());
                    }
                    return 0;
                }
                if(o1.getValue() instanceof Long){
                    Long l1= (Long) o1.getValue();
                    Long l2= (Long) o2.getValue();
                    return Long.compare(l1,l2);
                }
                return 0;
            }
        });
        Map<K,V>sortedMap=new LinkedHashMap<>();
        for(Map.Entry<K,V> entry:dataList){
            sortedMap.put(entry.getKey(),entry.getValue());
        }
        return sortedMap;
    }
}

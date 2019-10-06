/*
 * 2018, m6c7l
 */

package de.ikarion.xps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.ikarion.xps.data.Tuple;

public final class Utility {
   
    private Utility() {}

    public static Tuple json(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Object result = null;
        int i = 0;
        Class<?>[] clazz = new Class<?>[] {Map.class, List.class};
        while ((result == null) && (i < clazz.length)) {
            try { result = mapper.readValue(json, clazz[i]);
            } catch (IOException e) { }
            i++;
        }
        return json(result);
    }
    
    private static Tuple json(Object obj) {
        Tuple tuple = new Tuple();
        if (obj instanceof Map) {
            Map<?,?> temp = (Map<?,?>)obj;
            for (Object key : temp.keySet()) {
                tuple.add(key, json(temp.get(key)));
            }            
        } else if (obj instanceof List) {
            List<?> temp = (List<?>)obj;
            for (Object item : temp) {
                tuple.add(json(item));
            }            
        } else {
            return new Tuple(Utility.convert(obj));
        }
        return tuple;
    }

    public static Object convert(Object value) {
        if (value == null) return null;
        String s = value.toString();
        if (s.length() <= 18) {
            try {
                long l = Long.parseLong(s);
                if ((s.length() > 1) && s.startsWith("0")) {
                    return s;
                }
                if ((l <= Integer.MAX_VALUE) && (l >= Integer.MIN_VALUE)) {
                    return (int)l;
                }
                return l;
            } catch (NumberFormatException e) { }
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) { }        
        if (s.toLowerCase().equals("true") || s.toLowerCase().equals("false")) {
            return Boolean.parseBoolean(s);
        }        
        return s;
    }
    
    public static Object number(String d) {
        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(d);
        if (m.find()) return convert(m.group());
        return null;
    }

    public static List<List<Object>> zip(Object[] value1, Object[] value2) {
        int n = Math.min(value1.length, value2.length);
        List<List<Object>> result = new ArrayList<>(n);
        for (int i=0; i<n; ++i) {
            List<Object> items = new ArrayList<>(2);
            items.add(value1[i]); items.add(value2[i]);
            result.add(items);
        }   
        return result;
    }

    public static List<List<Object>> zip(Object[] value1, Object[] value2, Object[] value3) {
        int n = Math.min(value1.length, Math.min(value2.length, value3.length));
        List<List<Object>> result = new ArrayList<>(n);
        for (int i=0; i<n; ++i) {
            List<Object> items = new ArrayList<>(3);
            items.add(value1[i]); items.add(value2[i]); items.add(value3[i]);
            result.add(items);
        }   
        return result;
    }

    public static<T> int find(T[] a, T target) {
        return Arrays.asList(a).indexOf(target);
    }
    
    /*
    public static Map<?,?> json(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<?,?> map = new HashMap<>();
        try {
            map = mapper.readValue(json, Map.class);
        } catch (IOException e) {
        }        
        return map;
    }

    public static String json(Map<?,?> map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);
        return json;
    }
    
    public static String readFile(String filename) throws IOException {
        File f = new File(filename);
        byte[] bytes = Files.readAllBytes(f.toPath());
        return new String(bytes, "utf-8");
    }

    public static String prettify(String json) throws IOException {
        String s = json;
        StringBuilder sb = new StringBuilder();
        int level = 0;
        for (int i=0; i<s.length(); i++) {
            if ((s.charAt(i) == '{') && (s.charAt(i+1) != '}')) {
                if (i != 0) sb.append("\n");
                sb.append(new String(new char[level * 4]).replace("\0", " "));
                level++;
            } else if ((s.charAt(i) == '}') && (s.charAt(i-1) != '{')) {
                level--;                
            }
            sb.append(s.charAt(i));
            if (s.charAt(i) == ':') {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    */
}

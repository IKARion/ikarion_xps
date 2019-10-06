/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.ikarion.xps.Utility;
import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.collide.Model;
import de.ikarion.xps.data.moodle.Event;
import de.ikarion.xps.engine.Server;

public class Query {

    private static final String[] META = new String[] {
            Fact.class.getSimpleName().toLowerCase() + "s",
            Event.class.getSimpleName().toLowerCase() + "s",
            Model.class.getSimpleName().toLowerCase() + "s",
    };
    
    private Doc doc = null;
    
    public Query(String json) throws IllegalArgumentException, IOException {
        doc = new Doc(json);
        if (!doc.contains("session", "query")) throw new IllegalArgumentException("invalid");
    }

    private Object get(String key) {
        Tuple temp = doc.pick(key);
        if (temp != null) return Utility.convert(temp.value());
        return null; 
    }
        
    public  Object session() { return get("session"); }
    public  Object query()   { return get("query"); }
    public  Object course()  { return get("course"); }
    public  Object id()      { return get("id"); }
    public  Object from()    { return get("from"); }
    public  Object to()      { return get("to"); }

    public boolean meta() {
        return Arrays.asList(META).contains(this.query());
    }
    
    public String toString() {
        return doc.toString();
    }
    
    public final String execute(Server xps) {
        Query qy = this;
        ArrayList<Object> args = new ArrayList<Object>();
        if (qy.course() != null) { args.add(qy.course()); } else { args.add(null); } 
        if (qy.id() != null) { args.add(qy.id()); } else { args.add(null); }
        if (qy.from() != null) { args.add(qy.from()); } else { args.add(Long.MIN_VALUE); }
        if (qy.to() != null) { args.add(qy.to()); } else { args.add(Long.MAX_VALUE); }
        Object[] result = xps.engine().query(String.valueOf(qy.query()), args.toArray());
        if (result != null) {
            StringBuffer sb = new StringBuffer();
            sb.append(qy.toString() + "\n");
            // remove duplicates in result and sort elements; relies on equals, not on compareTo like TreeSet            
            if (result.length > 0) {
                List<Comparable<Object>> list = new ArrayList<Comparable<Object>>();
                for (int i=0; i<result.length; i++) {
                    if (!list.contains(result[i])) {
                        list.add((Comparable<Object>)result[i]);
                    }                                                                
                }
                if (list.size() > 0) {
                    Collections.sort(list);
                    Collections.reverse(list);               
                    for (Object o : list) sb.append(o.toString() + "\n");                                    
                }                
            }
            return sb.toString();
        }
        return null;
    }

    public void finalize() throws Throwable {
        super.finalize();
        doc = null;
    }

}

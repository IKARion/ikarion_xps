/*
 * 2018, m6c7l
 */

package de.ikarion.xps.engine;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.data.Data;
import de.ikarion.xps.data.Query;
import de.ikarion.xps.data.moodle.Event;
import de.ikarion.xps.data.collide.GroupModel;

/**
 * @Python
 *    import xmlrpc.client
 *    proxy = xmlrpc.client.ServerProxy("http://0.0.0.0:50100")
 *    print(proxy.xps.add("..."))
 */

public class Server {
        
    //private static final NumberFormat nform = new DecimalFormat("#0.000", new DecimalFormatSymbols(Locale.ROOT));
    private static final DateFormat dform = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat tform = new SimpleDateFormat("HH:mm:ss");
    
    private Engine xps = null;
    private long timeChanged = 0;
    private List<String> filter = null;      
    private Map<String,String> cache = null; 
    
    public Server(Engine engine, String[] args) {
        this.xps = engine;
        this.filter = Arrays.asList(args);
        this.cache = new HashMap<String,String>();
        this.xps.put(new Clock());
    }

    private static void log(Class<?> c, String s, String e) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Date date = new Date();
        PrintStream ps = System.out;
        map.put("date", dform.format(date));
        map.put("time", tform.format(date));
        map.put("type", c.getSimpleName());
        if (e!=null) {
            ps = System.err;
            map.put("error", e);
        }
        map.put("content", s);
        String res = map.toString();
        ps.println(res.trim()); //.substring(1, res.length() - 1));
    }

    public String add(String json) {
        
        if (json == null) return null;
        
        // --- DATA ---
        
        int n = Data.expire(this);
        if (n > 0) {
            Server.log(Data.class, "expired(" + n + ")", null);
        }

        // --- QUERY ---
        
        try {
            Query query = new Query(json);
            String result = null;
            if (this.cache.containsKey(json) && (!query.meta())) {
                result = this.cache.get(json);                
            } else {
                result = query.execute(this);                
                this.cache.put(json, result);
            }
            if (result != null) {
                Server.log(Query.class, query.toString(), null);
                return result;
            } else {
                Server.log(Query.class, query.toString(), "failed");
                return null;
            }
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (IOException ioe) {
            Server.log(Query.class, json, ioe.getLocalizedMessage());
            return null;
        }
        
        // --- EVENT ---
        
        try {
            Event ev = new Event(json);
            if (filter.size() == 0 || filter.contains(ev.courseid().toString())) {
                if (ev.valid(this)) {
                    Server.log(Event.class, ev.toString(), null);
                    this.xps.put(ev);
                } else {
                    Server.log(Event.class, ev.toString(), "invalid");
//                    ev.invalidate();
//                    this.xps.put(ev);
                    return null;
                }                
            } else {
                Server.log(Event.class, ev.toString(), "filtered");
                return null;                
            }
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (IOException ioe) {
            Server.log(Event.class, json, ioe.getLocalizedMessage());
            return null;
        }
        
        // --- GROUP MODEL ---
        
        try {
            GroupModel gm = new GroupModel(json);
            if (filter.size() == 0 || filter.contains(gm.course().toString())) {
                if (gm.valid(this)) {
                    Server.log(GroupModel.class, gm.toString(), null);
                    this.xps.put(gm);
                } else {
                    Server.log(GroupModel.class, gm.toString(), "invalid");
//                    gm.invalidate();
//                    this.xps.put(gm);
                    return null;
                }                
            } else {
                Server.log(GroupModel.class, gm.toString(), "filtered");
                return null;                
            }
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (IOException ioe) {
            Server.log(GroupModel.class, json, ioe.getLocalizedMessage());
            return null;
        }
        
        // --- INFERENCE ---

        Inference result = this.xps.infer(new String[] {"event", "model", "progress", "common"});
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<result.size(); i++) {
            sb.append(result.rule(i) + "(" + result.time(i) + ") ");            
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            String s = sb.toString();
            if (s.contains(":insert(") || s.contains(":forget(") ||
                    s.contains(":change(") || s.contains(":assign(") ||
                        s.contains(":detach(") || s.contains(":attach(") ||
                            s.contains(":append(") || s.contains(":reset(")) { // facts memory has been changed
                this.cache.clear();
            }
            Server.log(Inference.class, s, null);
        } else {
            Server.log(Server.class, json, "irrelevant");
        }

        // --- FACTS: NEW AND CHANGED ---
        
        Object[] objs = xps.list(Fact.class);
        long since = timeChanged;
        for (int i=0; i<objs.length; i++) {
            Fact fact = (Fact)objs[i];
            if (fact.changed() > timeChanged) timeChanged = fact.changed();
            if (fact.changed() > since) {
                Server.log(Fact.class, fact.toString(), null); 
            }
        }
        
        return null;

    }
    
    public String add(Map<String, Class<?>> map) {
        if (map == null) return null;
        return null;
    }
    
    public Engine engine() {
        return this.xps;
    }
    
    public long last() {
        return timeChanged;
    }

}

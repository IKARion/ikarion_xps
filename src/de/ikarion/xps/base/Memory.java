/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Memory {

    private LinkedHashMap<Long, ArrayList<Object>> entries = null;
    private ArrayList<Long> history = null;
    private Long added = null;
        
    protected Memory() {
        this.entries = new LinkedHashMap<Long, ArrayList<Object>>();
        this.history = new ArrayList<Long>();
    }

    public boolean add(Object value) {
        return this.add(System.currentTimeMillis(), value);
    }

    public boolean add(Long time, Object value) {
        if ((time != null)) {
            if (!entries.containsKey(time)) {
                history.add(time);
                entries.put(time, new ArrayList<Object>());
                Collections.sort(history);
            }
            entries.get(time).add(value);
            added = time.longValue();
            return true;
        }
        return false;
    }

    public Long latest() {
        return this.added;
    }
    
    public Object[] get(Long time) {
        ArrayList<Object> objs = entries.get(time);
        if (objs != null) return objs.toArray();
        return new Object[] {};
    }
    
    public Long first() {
        if (history.size() > 0) {
            return history.get(0);
        }
        return null;
    }

    public Long second() {
        if (history.size() > 1) {
            return history.get(1);
        }
        return null;
    }
    
    public Long penultimate() {
        if (history.size() > 1) {
            return history.get(history.size() - 2);
        }
        return null;
    }
    
    public Long last() {
        if (history.size() > 0) {
            return history.get(history.size() - 1);
        }
        return null;
    }
    
    public Long[] times() {
        return this.history.toArray(new Long[this.history.size()]);
    }
    
    public void finalize() throws Throwable {
        super.finalize();
        synchronized (this.history) { this.history.clear(); }
        this.history = null;
        synchronized (this.entries) { this.entries.clear(); }
        this.entries = null;            
    }
    
}
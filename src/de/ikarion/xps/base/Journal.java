/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Journal {

    private Hashtable<Object, Memory> memory = null;
    
    public Journal() {
        this.memory = new Hashtable<Object, Memory>();
    }

    public Object[] id() {
        return memory.keySet().toArray();
    }

    public boolean put(Long time, Object id, Object value) {
        if (id != null) {
            Memory mem = this.memory.get(id);
            if (mem == null) {
                mem = new Memory();
                this.memory.put(id, mem);
            }
            return mem.add(time, value);
        }
        return false;
    }

    public Long recent(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.latest();
        return null;
    }
    
    public Object latest(Object id) {
        Object[] result = this.get(id);
        if (result.length > 0) {
            return result[result.length - 1];
        }
        return null;
    }

    public Object[] get(Object id) {
        return this.get(id, this.last(id));
    }
    
    public Object[] get(Object id, Long time) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.get(time);
        return new Object[] {};
    }
    
    public Long first(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.first();
        return null;
    }

    public Long second(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.second();
        return null;
    }
    
    public Long penultimate(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.penultimate();
        return null;
    }

    public Long last(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.last();
        return null;
    }

    public Long last(Class<?> clazz) {
        Long temp = null;
        for (Object item : memory.keySet())
            if (item.getClass().equals(clazz))
                if ((temp == null) || (memory.get(item).last() > temp))
                    temp = memory.get(item).last();
        return temp;
    }

    public Object[] all(Class<?> clazz) {
        ArrayList<Object> temp = new ArrayList<Object>();
        for (Object item : memory.keySet()) {
            if (item.getClass().equals(clazz)) temp.add(item);
        }
        return temp.toArray();
    }

    public Object[] all(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) {
            Long[] times = mem.times();
            ArrayList<Object> result = new ArrayList<Object>();
            for (Long time : times) {
                Object[] values = mem.get(time);
                for (Object obj : values) {
                    result.add(obj);
                }
            }
            return result.toArray();
        }
        return new Object[] {};
    }

    public Map<Class<?>, Object[]> map(Object id) {
        Map<Class<?>, Object[]> result = new HashMap<Class<?>, Object[]>();
        Memory mem = this.memory.get(id);
        if (mem != null) {
            Long[] times = mem.times();
            Map<Class<?>, ArrayList<Object>> temp = new HashMap<Class<?>, ArrayList<Object>>();
            for (Long time : times) {
                Object[] values = mem.get(time);
                for (Object obj : values) {
                    if (!temp.containsKey(obj.getClass())) {
                        temp.put(obj.getClass(), new ArrayList<Object>());
                    }
                    temp.get(obj.getClass()).add(obj);
                }
            }
            for (Class<?> clazz : temp.keySet()) {
                result.put(clazz, temp.get(clazz).toArray());
            }
        }
        return result;
    }
    
    public Long[] history(Object id) {
        Memory mem = this.memory.get(id);
        if (mem != null) return mem.times();
        return new Long[] {};
    }
    
    public int count(Object id) {
        int cnt = 0;
        Long[] times = history(id);
        for (Long time : times) {
            cnt += get(id, time).length;
        }
        return cnt;
    }
    
    public void finalize() throws Throwable {
        super.finalize();
        synchronized (this.memory) { this.memory.clear(); }
        this.memory = null;            
    }
    
}

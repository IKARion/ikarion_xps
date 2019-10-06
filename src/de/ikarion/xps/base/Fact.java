/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base;

import java.util.ArrayList;
import java.util.Map;

import de.ikarion.xps.data.Tuple;

public abstract class Fact implements Comparable<Fact> {
    
    public static boolean verbose = false;
        
    protected Journal journal = null; 

    public Fact(Long time, Object id) {
        this(time, id, null);
    }

    public Fact(Long time, Object id, Fact parent) {
        this.journal = new Journal();
        this.journal.put(time, "id", id);
        if (parent != null) this.journal.put(time, "parent", parent);
    }

    public Object id() {
        return journal.latest("id");           
    }

    public boolean forgotten() {
        return this.id() == null;
    }
    
    public long created() {
        return journal.first("id");
    }
    
    public long changed() {
        long last = 0;
        Object[] ids = journal.id();
        for (Object id : ids) {
            if (journal.last(id) > last) {
                last = journal.last(id);
            }
        }
        return last;
    }
    
    public boolean forget(Long time) {
        return (this.id() != null) && this.journal.put(time, "id", null);
    }
    
    // ----------------------------------

    protected final <T> Object[] childs(Class<T> clazz) {
        Object[] obj = journal.all(clazz);
        ArrayList<T> temp = new ArrayList<T>();
        for (Object key : obj) {
            Object[] items = journal.get(key); // all values of most recent time
            if ((boolean)items[items.length - 1])
                temp.add((T)key);
            //temp.add((T)key);
        }
        return temp.toArray();
    }

    public Fact[] parents() {
        Map<Class<?>, Object[]> obj = journal.map("parent");
        if (obj.size() == 0) return new Fact[] {};
        Fact[] temp = new Fact[obj.keySet().size()];
        int i = 0;
        for (Class<?> key : obj.keySet()) {
            Object[] value = obj.get(key);
            temp[i] = ((Fact)(value[value.length - 1]));
            i++;
        }
        return temp;
    }
    
    public Fact parent() {
        return (Fact)this.journal.latest("parent");
    }

    public boolean parent(Long time, Fact parent) {
        return journal.put(time, "parent", parent);
    }
    
    // ----------------------------------
    
    public final Object[] annotations(Object label) {
        return journal.all(label);
    }
   
    public final Object annotation(Object label) {
        return this.journal.latest(label);
    }

    public final boolean annotation(Long time, Object label, Object value) {
        return this.journal.put(time, label, value);
    }
    
    // ----------------------------------
    
    public String toString() {
        return this.toString(new Object[] {});
    }
    
    protected String toString(Object... append) {
        return String.format("{\"%s\":{\"id\":%s,\"created\":%d,\"changed\":%d%s}}",
                getClass().getSimpleName().toLowerCase(),
                (!(id() instanceof Fact)) ? "\"" + id() + "\"" : id(),
                created(),
                changed(),
                concat(append));
    }

    protected static String concat(Object... items) {
        if ((items == null) || (items.length==0)) return "";
        ArrayList<String> arr = new ArrayList<String>();
        for (Object item : items) {
            if (item instanceof Tuple) {
                arr.add(strip(item.toString()));
            } else if (item != null) {
                arr.add(item.toString());
            }
        }
        return "," + String.join(",", arr);
    }

    private static final String strip(String s) {
        if (s.startsWith("{") && (s.endsWith("}")))
            return s.substring(1, s.length() - 1);
        return s;
    }
    
    public int compareTo(Fact fact) {
        return Long.compare(this.changed(), fact.changed());
    }
    
    public boolean equals(Object other) {
        if ((other == null) || (!(other instanceof Fact))) return false;
        Fact fact = (Fact)other;
        return this.getClass()==fact.getClass() &&
                this.id()==fact.id() &&
                 this.parent()==fact.parent() &&
                  this.created()==fact.created();
    }

    public void finalize() throws Throwable {
        super.finalize();
        this.journal = null;
    }
    
}

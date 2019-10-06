/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;

public class Tuple implements Serializable {
    
    private Object value = null;
    private List<Tuple> values = null;
    private boolean multi = false;
    
    public Tuple() {}

    public Tuple(Object value) {
        this();
        this.value(value);
    }

    public Tuple(Object[] values) {
        this();
        this.add(this, values);
        this.multi = true;
    }
    
    public Tuple(Object value, Object... values) {
        this(values);
        this.value(value);
    }

    // --- this value (r/w) ---

    public final Object value() {
        return this.value;
    }

    public final Tuple value(Object value) {
        this.value = value;
        return this;
    }

    // --- this space (r) ---

    public final Tuple[] get() {
        Tuple[] tuples = new Tuple[this.size()];
        for (int i=0; i<tuples.length; i++) tuples[i] = this.get(i);
        return tuples;
    }

    public final int size() {
        if (this.values == null) return 0;
        return this.values.size();
    }
    
    public final int indexOf(Tuple tuple) {
        return this.values.indexOf(tuple);
    }

    public final int lastIndexOf(Tuple tuple) {
        return this.values.lastIndexOf(tuple);
    }

    public final int indexOf(Object value) {
        return this.indexOf(new Tuple(value));
    }

    public final int lastIndexOf(Object value) {
        return this.lastIndexOf(new Tuple(value));
    }
    
    public final Tuple get(int index) {
        if (this.values == null) throw new IndexOutOfBoundsException();
        return this.values.get(index);
    }
    
    public final Tuple first() {
        if (this.size() > 0) return this.get(0);
        return null;
    }
    
    public final Tuple last() {
        int len = this.size();
        if (len > 0) return this.get(len -1);
        return null;
    }

    public final Tuple get(String value) {
        if (this.contains(value)) return this.get(this.indexOf(value));
        return null;
    }

    public final Tuple pick(String value) {
        return this.pick(value, null);
    }

    public final Tuple pick(String value, Integer index) {
        Tuple entry = this.get(value);
        if (entry == null) {
            if (this.list()) {
                for (Tuple item : this.values) {
                    Tuple temp = item.get(value);
                    if (temp != null) {
                        entry = temp;
                        while (entry.host()) {
                            entry = entry.first();
                        }
                        break;
                    }
                }
            } else if (this.host()) {
                if ((value != null) && (value.equals(this.first().value()))) {
                    entry = this.first();                
                }
            } else if (this.pair()) {
                if ((value != null) && (value.equals(this.value()))) {
                    entry = this;                
                }
            }
        }
        if (entry != null) {
            entry = entry.first();
            if (entry.host() && this.list()) { 
                entry = entry.first();
            }
            if (index != null) {
                if ((index > -1) && (index < entry.size())) {
                    entry = entry.get(index);
                } else {
                    entry = null;
                }    
            }
        }
        return entry;
    }

    public final boolean contains(Object... values) {
        if (this.values == null) return false;
        for (Object value : values) {
            if ((value instanceof Tuple) && (!this.values.contains((Tuple)value))) {
                return false;                    
            } else if (!this.values.contains(new Tuple(value))) {
                return false;
            }
        }
        return true;
    }
    
    // --- this space (w) ---
    
    public final void add(Tuple... tuples) {
        for (int i=0; i<tuples.length; i++) this.add(tuples[i]);
    }
    
    public final boolean add(Tuple tuple) {
        if (this.values == null) this.values = new ArrayList<Tuple>();
        if (!this.values.add(tuple)) return false;
        return true;
    }

    // ------------------
    
    // --- next space ---

    // create new tuple in space
    public final Tuple add(Object value) {
        Tuple tuple = new Tuple(value); 
        if (this.add(tuple)) return tuple;
        return null;
    }

    // --- flat space ---
    
    // create new tuple in space and add newly created tuples to the new tuple space (be flat)
    public final Tuple add(Object value, Object... values) {
        return this.add(this.add(value), values);
    }
    
    private final Tuple add(Tuple parent, Object... values) {
        for (int i=0; i<values.length; i++) {
            if (values[i] instanceof Tuple) {
                parent.add((Tuple)values[i]); 
            } else {
                parent.add(values[i]);   
            }   
        }
        return parent;
    }

    // --- deep space ---
    
    // create new tuple in space and put newly created tuples to the previous tuple space (go deep)
    public final Tuple[] add(Object[] values) {
        Tuple[] tuples = new Tuple[values.length];
        tuples[0] = this.add(values[0]);
        for (int i=1; i<values.length; i++) {
            tuples[i] = tuples[i - 1].add(values[i]);
        }
        return tuples;
    }

    // --- more space ---

    public final Tuple all() {
        Tuple result = new Tuple();
        for (int i=0; i<this.size(); i++) {
            result.add(this.get(i));
        }
        return result;
    }

    public final Tuple flat() {
        return new Tuple("", this.flatten(this).toArray());
    }

    private final List<Object> flatten(Tuple tuple) {
        List<Object> values = new ArrayList<Object>();
        if (tuple.value() != null) values.add(tuple.value());
        for (int i=0; i<tuple.size(); i++) {
            values.addAll(this.flatten(tuple.get(i)));
        }        
        return values;
    }
    
    // --- both space (r)
    
    public final boolean has(Object... values) {
        for (Object value : values) {
            if (value.equals(this.value)) return true;
        }
        return this.contains(values);
    }
    
    public final boolean blank() {
        return (this.size() == 0) && (this.value() == null);
    }

    public final boolean single() {
        return (this.size() == 0) && (this.value() != null);
    }

    public final boolean host() {
        return (this.size() == 1) && (this.value() == null);
    }
    
    public final boolean pair() {
        return (this.size() == 1) && (this.value() != null);
    }

    public final boolean list() {
        return (this.size() >  1) && (this.value() == null);
    }

    public final boolean map() {
        return (this.size() >  1) && (this.value() != null);
    }
    
    // --- override
    
    public boolean equals(Object o) {
      if (o == null) return false;
      if (o == this) return true;
      if (o instanceof Tuple) {
          if (((Tuple)o).value == null) return false;
          if (((Tuple)o).value == this.value) return true;
          if (((Tuple)o).toString().equals(this.toString())) return true;
      }
      return false;
    }
    
    public void finalize() throws Throwable {
        super.finalize();
        this.value = null;
        if (this.values != null) {
            synchronized (this.values) { this.values.clear(); }
            this.values = null;            
        }
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.pair()) {
            sb.append("{");
            sb.append("\"" + this.value + "\":");
            sb.append(StringUtils.join(this.values, ","));
            sb.append("}");   
        } else if (this.map()) {
            sb.append("{");
            sb.append("\"" + this.value + "\":");
            sb.append("[");
            sb.append(StringUtils.join(this.values, ","));
            sb.append("]");
            sb.append("}");                        
        } else if (this.list()) {
            sb.append("[");
            sb.append(StringUtils.join(this.values, ","));
            sb.append("]");
        } else if (this.host()) {
            sb.append(StringUtils.join(this.values, ","));
        } else if (this.single()) {
            if ((this.value instanceof Number) ||
                (this.value instanceof Boolean) ||
                (this.value instanceof Tuple) ||
                (this.value instanceof Tuple.Raw)) {  // avoid escapings
                sb.append(this.value);
            } else {
                sb.append("\"" + StringEscapeUtils.escapeJava(this.value.toString()) + "\"");
                if (multi) sb.append(":null");
            }
        } else if (this.blank()) {
            if (multi) {
                sb.append("{}");    
            } else {
                sb.append("null");
            }
        }
        return sb.toString();
    }

    public static final class Raw {
        
        private Object data = "";
        
        public static final Raw[] raw(Object[] objects) {
            Raw[] raw = new Raw[objects.length];
            for (int i=0; i<raw.length; i++) raw[i] = Raw.raw(objects[i]);
            return raw;
        }

        public static final Raw raw(Object object) {
            return new Raw(object);
        }

        private Raw(Object data) {
            this.data = data;
        }

        public String toString() {
            if (data != null) return data.toString();
            return null;
        }
        
    }
    
}

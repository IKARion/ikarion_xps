/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ikarion.xps.Utility;
import de.ikarion.xps.engine.Server;

public abstract class Data implements Comparable<Data> {
   
    public static int KEEP_ALIVE_PERIOD = 60 * 15; // in secs, 15 min

    protected Doc doc = null;
    
    private int time = 0;
    private boolean discarded = false;
    
    public Data(String json) throws IOException {
        doc = new Doc(json);
        time = (int)(System.currentTimeMillis() / 1000);       
    }
    
    public static final Object get(Object node, String key) {
        Tuple temp = bag(node, key);
        return temp != null ? Utility.convert(temp.value()) : null;
    }

    public static final Tuple bag(Object node, String key) {
        if (!(node instanceof Tuple)) return null;
        return ((Tuple)node).pick(key);
    }

    public static final Tuple bag(Object node, String key, int index) {
        if ((!(node instanceof Tuple)) || (index < 0)) return null;
        Tuple tuple = null;
        tuple = ((Tuple)node).get(index).pick(key);
        if (tuple == null) {
            tuple = ((Tuple)node).pick(key);
        }
        return tuple;
    }
    
    public static final Object[] all(Object node, String key) {
        if (!(node instanceof Tuple)) return new Object[] {};
        List<Object> result = new ArrayList<Object>();
        Tuple tuple = (Tuple)node;
        Tuple temp = null;
        for (int i=0; i<tuple.size(); i++) {
            temp = tuple.get(i).pick(key);
            if (temp != null) {
                result.add(Utility.convert(temp.value()));
            }
        }                        
        return result.toArray();
    }

    public final void discard() {
        this.discarded = true;
    }

    public final boolean discarded() {
        return this.discarded;
    }

    public final int stamping() {
        return time;
    }

    public int compareTo(Data data) {
        return Integer.compare(this.time, data.time);
    }
    
    public static final int expire(Server xps) {
        int n = 0;
        int k = 0;
        int t = (int)(System.currentTimeMillis() / 1000);
        List<Object> data = Arrays.asList(xps.engine().list(Data.class));
        while (k < data.size()) {
            Data o = (Data)data.get(k);
            if (o.stamping() + KEEP_ALIVE_PERIOD < t) {
                n++;
                xps.engine().remove(o);
            } else {
                break;
            }
            k++;
        }
        return n;
    }

    public void finalize() throws Throwable {
        super.finalize();
        doc = null;
    }
    
}

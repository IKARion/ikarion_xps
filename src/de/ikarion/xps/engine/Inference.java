package de.ikarion.xps.engine;

import java.util.ArrayList;
import java.util.List;

public class Inference {

    private long last = System.currentTimeMillis();
    
    private List<Container> fired = null;
    
    public Inference() {
        fired = new ArrayList<Container>();
    }

    public boolean add(String group, String rule) {
        return this.add(group, rule, null);
    }

    public boolean add(String group, String rule, Object object) {
        long actual = System.currentTimeMillis();
        if (fired.add(new Container( (int)(actual - last), group, rule, object))) {
            last = actual;
            return true;
        }
        return false;
    }
    
    public int size() {
        return this.fired.size();
    }
    
    public int time(int index) {
        return fired.get(index).time;
    }

    public String rule(int index) {
        return fired.get(index).rule;
    }

    public String group(int index) {
        return fired.get(index).group;
    }

    public Object object(int index) {
        return fired.get(index).object;
    }

    public boolean contains(String group, String rule) {
        for (int i=0; i<fired.size(); i++) {
            if (fired.get(i).group.equals(group) &&
                    fired.get(i).rule.equals(rule)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(Object obj) {
        Object[] objs = all();        
        for (int i=0; i<objs.length; i++) {
            if (String.valueOf(objs[i]).equals(String.valueOf(obj))) {
                return true;
            }
        }        
        return false;
    }
    
    public Object[] all() {
        ArrayList<Object> temp = new ArrayList<Object>();
        for (int i=0; i<fired.size(); i++) {
            if (fired.get(i).object != null) {
                temp.add(fired.get(i).object);
            }
        }
        return temp.toArray();
    }
    
    private static class Container {

        private int time = 0;
        private String group = null;
        private String rule = null;
        private Object object = null;
        
        public Container(int time, String group, String rule, Object object) {
            this.time = time;
            this.group = group;
            this.rule = rule;
            this.object = object;
        }

    }
    
}

package de.ikarion.xps.engine.test.threesons;

public class Age implements Comparable<Age> {

    private int value;

    public Age(int value) {
        this.setValue(value);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    public String toString() {
        return "" + this.value; 
    }

    public int compareTo(Age o) {
        return Integer.compare(this.value, o.value); 
    }
    
}

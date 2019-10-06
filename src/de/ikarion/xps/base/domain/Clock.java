package de.ikarion.xps.base.domain;

public class Clock {

    private Long time = null;
    private Integer dt = null;

    public Clock() {}
    
    public Clock(long epoch) {
        this();
        this.time = epoch;
    }
    
    public Long value() {     
        return time;
    }

    public Long previous() {
        if ((this.time != null) && (this.dt != null))
            return this.time - this.dt;
        return null;
    }

    public Integer delta() {
        return dt;
    }
    
    public void value(long time) {        
        if (this.time != null) {
            int dt = (int)(time - this.time);
            if (dt < 0) return;
            this.dt = dt;
        }
        this.time = time;
    }

    public static long current() {
        return System.currentTimeMillis() / 1000;
    }
    
}

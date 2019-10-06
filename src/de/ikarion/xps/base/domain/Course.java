/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;

public class Course extends Fact {

    private Long[] period = new Long[] {null, null};
    
    public Course(Long time, Object id) {
        super(time, id);
    }
    
    public String toString() {
        if ((period[0] != null) && (period[1] != null)) {
            return super.toString(
                    new Tuple("period", period[0], period[1])
                    );
        }
        return super.toString();
    }
    
    public Long[] period() {
        return this.period;
    }

    public void period(long from, long to) {
        this.period[0] = from;
        this.period[1] = to;
    }

}

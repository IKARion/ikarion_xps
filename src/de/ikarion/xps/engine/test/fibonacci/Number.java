/*
 * 2018, m6c7l
 */

package de.ikarion.xps.engine.test.fibonacci;

import de.ikarion.xps.data.Tuple;

public class Number extends Tuple {

    public Number(final int index) {
        super(index, -1L);
    }

    public void setValue(long value) {
        super.first().value(value);
    }
    
    public long getValue() {
        return (long)(super.first().value());
    }

    public int getIndex() {
        return (int)super.value();
    }
    
}

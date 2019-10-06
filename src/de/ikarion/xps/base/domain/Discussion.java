/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;

public class Discussion extends Fact {
    
    public Discussion(Long time, Object id, Fact parent) {
        super(time, id, parent);
    }
   
    public String toString() {
        return super.toString(
                Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }
    
}

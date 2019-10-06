/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;

public class Grouping extends Fact {
    
    public Grouping(Long time, Object id, Fact parent) {
        super(time, id, parent);
    }

    public Object[] groups() {
        return super.childs(Group.class);
    }

    public boolean group(Long time, Group group, boolean forget) {
        return journal.put(time, group, !forget);
    }
    
    public String toString() {
        return super.toString(
                new Tuple("groups", Tuple.Raw.raw(groups()))
                );
    }

}

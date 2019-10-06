/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;

public class Group extends Fact {
        
    public Group(Long time, Object id, Fact parent) {
        super(time, id, parent);
    }
    
    public Object[] members() {
        return super.childs(Member.class);
    }
    
    public boolean member(Long time, Member member, boolean forget) {
        return journal.put(time, member, !forget);
    }
    
    public String toString() {
        return super.toString(
                new Tuple("members", Tuple.Raw.raw(members())),
                new Tuple("other",
                        new Tuple("gini", super.annotation("gini"))
                    )
                );
    }
       
}

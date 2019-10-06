/*
 * 2019, m6c7l
 */

package de.ikarion.xps.base.awareness;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Member;
import de.ikarion.xps.data.Tuple;

public class Assessment extends Fact implements Awareness {
    
    public Assessment(Long time, Member member, Tuple items) {
        super(time, "", member);
        journal.put(time, "items", items);
    }
    
    public Tuple items() {
        if (journal.count("items") > 0) {
            Object[] result = journal.get("items");
            return (Tuple)result[result.length - 1];
        }
        return null;
    }

    public boolean group() {
        return false;
    }
    
    public Long submitted() {
        return super.created();
    }
    
    public Long submitted(Member member) {
        return null;
    }
    
    public String toString() {
        return super.toString(
                new Tuple("items", items()),
                Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }

}

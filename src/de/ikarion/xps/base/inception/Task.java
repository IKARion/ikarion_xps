/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.inception;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.domain.Module;
import de.ikarion.xps.data.Tuple;

public class Task extends Fact {
    
    public Task(Long time, Object id, Fact parent, Long from, Long to) {
        super(time, id, parent);
        journal.put(time, "from", from);
        journal.put(time, "to", to);
    }

    public long from() {
        return (Long)journal.get("from")[0];
    }

    public long to() {
        return (Long)journal.get("to")[0];
    }

    public Object[] groups() {
        return super.childs(Group.class);
    }
    
    public boolean group(Long time, Group group, boolean forget) {
        return journal.put(time, group, !forget);
    }
    
    public Object[] modules() {
        return super.childs(Module.class);
    }

    public boolean module(Long time, Module module, boolean forget) {
        return journal.put(time, module, !forget);
    }
    
    public String toString() {
        return super.toString(
                new Tuple("groups", Tuple.Raw.raw(groups())),
                new Tuple("modules", Tuple.Raw.raw(modules())),
                new Tuple("period", from(), to())
                );
    }

}

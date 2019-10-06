/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;

public abstract class Module extends Fact {

    public Module(Long time, Object id, Object context, Fact parent) {
        super(time, id, parent);
        if (context != null) this.journal.put(time, "context", context);
    }

    public final boolean id(Long time, Object value) {
        return this.journal.put(time, "id", value);
    }
    
    public final Object context() {
        if (journal.count("context") > 0) {
            Object[] result = journal.get("context");
            return result[result.length - 1];
        }
        return null;
    }

    public String toString() {
        return super.toString(
                new Tuple("context", context())
                );
    }
    
}

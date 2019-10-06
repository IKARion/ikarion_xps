/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;

public class Member extends Fact {
    
    public Member(Long time, Object id, Fact parent) {
        super(time, id, parent);
    }
    
    public Object[] actions() {
        return journal.all("actions");
    }

    public boolean action(Long time, Object action) {
        return journal.put(time, "actions", action);
    }

    public Object[] views() {
        return journal.all("views");
    }

    public boolean view(Long time, Object view) {
        return journal.put(time, "views", view);
    }
    
    public String toString() {
        return super.toString(
                new Tuple("actions",
                        new Tuple("views", views().length),
                        new Tuple("active", actions().length)
                    ),
                new Tuple("other",
                        new Tuple("forum", super.annotation("forum")),
                        new Tuple("wiki", super.annotation("wiki"))
                    )
                );
    }
    
}

/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;

public class Forum extends Module {

    public Forum(Long time, Object context, Fact parent) {
        super(time, "", context, parent);
    }
    
    public Forum(Long time, Object id, Object context, Fact parent) {
        super(time, id, context, parent);
    }

}

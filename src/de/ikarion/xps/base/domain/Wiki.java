/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;

public class Wiki extends Module {

    public Wiki(Long time, Object context, Fact parent) {
        super(time, "", context, parent);
    }
    
    public Wiki(Long time, Object id, Object context, Fact parent) {
        super(time, id, context, parent);
    }
        
}

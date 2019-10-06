/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import de.ikarion.xps.base.Fact;

public class Post extends Content {
   
    public Post(Long time, Object id, Fact parent) {
        super(time, id, parent);
    }

}

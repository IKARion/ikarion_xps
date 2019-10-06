/*
 * 2019, m6c7l
 */

package de.ikarion.xps.base.awareness;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Member;

public interface Awareness {

    public Object id();
    
    public boolean forgotten();
    public long created();    
    public long changed();
    public boolean forget(Long time);
    
    public Long submitted();
    public Long submitted(Member member);
    
    public boolean group();
    
    public Fact[] parents();    
    public Fact parent();

}
/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.prompt;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Group;

public abstract class Prompt extends Fact {
    
    protected Clock clock = null;
    
    public Prompt(Clock clock, Fact parent) {
        super(clock.value(), "", parent);
        this.clock = clock;
    }
    
    public boolean group() {
        return super.parent() instanceof Group;
    }
    
    public abstract Double value();

}

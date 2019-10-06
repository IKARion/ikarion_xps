/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.prompt;

import java.util.List;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Content;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.inception.Task;
import de.ikarion.xps.data.Tuple;

public class Preparation extends Prompt {
    
    public static final double DEFAULT_VALUE = 0.0;
    
    public static int WARMUP_PERIOD = 4 * 24 * 60 * 60; // seconds /* 4 Tage */
    
    public Preparation(Clock clock, Group group) {
        super(clock, group);
        journal.put(clock.value(), "value", DEFAULT_VALUE);
    }

    public boolean apply(Task task, List<Content> contents) {
        Double value = DEFAULT_VALUE;
        if (task != null && !task.forgotten() && (contents != null)) {
            int coord = contents.size();
            if (clock.value() > task.from() + WARMUP_PERIOD) {
                value = 1.0 - Math.min(1.0, coord * 0.75);
            }            
        }
        if (value != value()) {
            journal.put(clock.value(), "value", value);
            return true;
        }
        return false;
    }

    public Double value() {
        return (Double)journal.latest("value");
    }

    public String toString() {
        return super.toString(
                    new Tuple("value", value()),
                    Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }
        
}

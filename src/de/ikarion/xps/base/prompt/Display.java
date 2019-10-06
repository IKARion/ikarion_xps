/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.prompt;

import java.util.List;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.awareness.Assessment;
import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Member;
import de.ikarion.xps.base.inception.Task;
import de.ikarion.xps.data.Tuple;

public class Display extends Prompt {
    
    public static final double ASSESSMENT_BLOCK   = 0.0;
    public static final double ASSESSMENT_DEFAULT = 0.5;
    public static final double ASSESSMENT_FORCE   = 1.0;
    
    public static int ASSESSMENT_PERIOD_BLOCK =          30 * 60; // seconds  /* 30 Minuten */
    public static int ASSESSMENT_PERIOD_FORCE = 3 * 24 * 60 * 60; // seconds  /* 3 Tage */
    
    public Display(Clock clock, Member member) {
        super(clock, member);
        journal.put(clock.value(), "assessment", ASSESSMENT_DEFAULT);
    }

    public boolean assessment(Task task, List<Assessment> assessments) {
        Double value = ASSESSMENT_DEFAULT; // release
        if (task != null && !task.forgotten() && (assessments != null)) {
            long time = task.from();
            for (Assessment a : assessments) {
                if (a.created() > time) time = a.created();
            }
            if (time + ASSESSMENT_PERIOD_BLOCK > clock.value()) {
                value = ASSESSMENT_BLOCK; // block
            } else if (time + ASSESSMENT_PERIOD_FORCE < clock.value()) {
                value = ASSESSMENT_FORCE; // force
            }            
        } else {
            value = ASSESSMENT_BLOCK; // block
        }
        if (value != value("assessment")) {
            journal.put(clock.value(), "assessment", value);    
            return true;
        }
        return false;
    }

    public Double value() {
        return null;
    }

    private Double value(String key) {
        return (Double)journal.latest(key);
    }
    
    public String toString() {
        return super.toString(
                    new Tuple("assessment", value("assessment")),
                    Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }

}

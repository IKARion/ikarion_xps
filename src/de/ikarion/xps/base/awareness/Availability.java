/*
 * 2019, m6c7l
 */

package de.ikarion.xps.base.awareness;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.domain.Member;
import de.ikarion.xps.data.Tuple;

public class Availability extends Fact implements Awareness {
    
    private static final Calendar cal = Calendar.getInstance();
    private Clock clock = null;
    
    public Availability(Clock clock, Group group) {
        super(clock.value(), "", group);
        this.clock = clock;
    }
    
    public boolean value(Long time, Member member, Tuple dates, Byte tz, Byte st) {

        //System.out.println("# availability " + time + " " + member.id() + " " + dates);

        if (dates.size() == 7) { /* WEEK ITEM */
            
            boolean[] week = new boolean[7 * 24];

            for (int i=0; i<dates.size(); i++) {
                if (!dates.get(i).first().blank()) {
                    
                    String dow = dates.get(i).value().toString();
                    Tuple tuple = dates.get(i).pick(dow);
                    
                    int mul = 0;
                    
                    switch (dow) {
                        case "sun": mul = Calendar.SUNDAY; break;
                        case "mon": mul = Calendar.MONDAY; break;
                        case "tue": mul = Calendar.TUESDAY; break;
                        case "wed": mul = Calendar.WEDNESDAY; break;
                        case "thu": mul = Calendar.THURSDAY; break;
                        case "fri": mul = Calendar.FRIDAY; break;
                        case "sat": mul = Calendar.SATURDAY; break;
                    }
                    
                    mul = mul - 1;
                    
                    if (mul < 0) return false;
                    
                    int[] from = new int[tuple.size()];
                    int[] to = new int[tuple.size()];

                    try {
                        if (tuple.list() || tuple.host()) {
                            for (int j=0; j<tuple.size(); j++) {
                                
                                String fval = tuple.get(j).pick("from").value().toString();                            
                                if (fval.length() == 4) fval = fval.substring(0, 2);
                                while (fval.startsWith("0")) fval = fval.substring(1);
                                from[j] = Integer.valueOf(fval);
                                
                                String tval = tuple.get(j).pick("to").value().toString();
                                if (tval.length() == 4) tval = tval.substring(0, 2);
                                while (tval.startsWith("0")) tval = tval.substring(1);
                                to[j] = Integer.valueOf(tval);
                            }
                        }                        
                    } catch (Exception e) {
                        return false;
                    }
                    
                    int ofs = tz + st;
                    for (int j=0; j<from.length; j++) {
                        for (int k=mul*24+from[j]-ofs; k<mul*24+to[j]-ofs; k++) {
                            week[k >= 0 ? k : k + week.length] = true;
                        }
                    }
                    
                }
            }
            
            return journal.put(time, member, week);

        }

        return false;
    }

    public Object[] members() {
        return journal.all(Member.class);
    }

    public Tuple proposal() {
        
        Object[] membs = members();
        
        float[] week = new float[24 * 7];
        for (int i=0; i<membs.length; i++) {
            boolean[] w = (boolean[])journal.latest(membs[i]);
            if (w.length != week.length) continue;
            for (int k=0; k<w.length; k++) {
                week[k] += w[k] ? 1 : 0;
            }            
        }
        
        for (int k=0; k<week.length; k++) {
            week[k] /= membs.length;
        }            
        
        List<Float> b = Arrays.asList(ArrayUtils.toObject(week));

        long current = clock.value();
        cal.setTime(new Date(current * 1000));
        int day = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1; // 1 to 7 for Calendar to 0 to 6
        
        long start = (current - current % 86400) - day * 86400; // start of current week        
        long from = 0;
        long to = 0;
        float max = Collections.max(b);
        
        for (int m=0; m<week.length*2; m++) { // hour slots of two weeks in advance
            long v = start + (m * 3600);
            int n = m % week.length;
            if (v > current) {
                if ((week[n] == max) && (from == 0)) from = v;
                if ((week[n] != max) && (from != 0)) { to = v; break; }                
            }
        }

        if ((from == 0) || (to == 0)) return null;
        
        Tuple tup = new Tuple();        
        tup.add(new Tuple("from", from));
        tup.add(new Tuple("to", to));
        tup.add(new Tuple("match", max));
        tup.add(new Tuple("hours", Math.abs((from - current) / 3600)));
        
        return tup;
    }
    
    public Long submitted() {
        return journal.last(Member.class);
    }

    public Long submitted(Member member) {
        return journal.last(member);
    }

    public boolean group() {
        return true;
    }
    
    public Integer[] count() {
        Group group = (Group)super.parent();        
        return new Integer[] {journal.all(Member.class).length, group.members().length};
    }
    
    public String toString() {
        return super.toString(
                new Tuple("proposal", proposal()),
                new Tuple("count", Tuple.Raw.raw(count())),
                Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }

}

/*
 * 2019, m6c7l
 */

package de.ikarion.xps.base.awareness;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.domain.Member;
import de.ikarion.xps.data.Tuple;

public class Mood extends Fact implements Awareness {
    
    public Mood(Clock clock, Group group) {
        super(clock.value(), "", group);
    }

    public Object[] members() {
        return journal.all(Member.class);
    }

    public Object[] values(Member member) {
        return journal.all(member);
    }
    
    public boolean value(Long time, Member member, Float value) {
        //System.out.println("# mood " + time + " " + member.id() + " " + value);
        if (value > 1) value = value / 100.0f; // 0 -> 0, 50 -> 0.5, 100 -> 1.0
        return journal.put(time, member, value) && journal.put(time, value, null);        
    }
    
    public Double rating() {
        int a1 = journal.all(0.0f).length;
        int a2 = journal.all(0.5f).length;
        int a3 = journal.all(1.0f).length;
        int a = a1 + a2 + a3;
        if (a == 0) return null;
        return (0.0 * a1 + 0.5 * a2 + 1.0 * a3) / a;
    }
    
    public Double percentage(int[] items) {
        int a = items[0] + items[1] + items[2];
        if (a == 0) return null;
        return (0.0 * items[0] + 0.5 * items[1] + 1.0 * items[2]) / a;
    }

    public Tuple votes(int[] items) {
        Tuple tup = new Tuple();        
        tup.add(new Tuple(0.0, items[0]));
        tup.add(new Tuple(0.5, items[1]));
        tup.add(new Tuple(1.0, items[2]));
        return tup;
    }

    private int[] items() {
        int[] res = new int[] {0, 0, 0};
        Object[] membs = members();
        for (int i=0; i<membs.length; i++) {
            switch ((int)(((Float)journal.latest(membs[i])) * 10)) {
            case 0:  res[0]++; break;
            case 5:  res[1]++; break;
            case 10: res[2]++; break;                
            }
        }
        return res;
    }

    public Integer[] count() {
        Group group = (Group)super.parent();        
        return new Integer[] {journal.all(Member.class).length, group.members().length};
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
    
    public String toString() {
        int[] items = items();
        return super.toString(
                new Tuple("percentage", Tuple.Raw.raw(percentage(items))),
                new Tuple("votes", this.votes(items)),
                new Tuple("count", Tuple.Raw.raw(count())),
                new Tuple("rating", Tuple.Raw.raw(rating())),
                Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }

}

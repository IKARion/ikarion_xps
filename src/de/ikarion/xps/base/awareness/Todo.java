/*
 * 2019, m6c7l
 */

package de.ikarion.xps.base.awareness;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.domain.Member;
import de.ikarion.xps.data.Tuple;

public class Todo extends Fact implements Awareness {
    
    public Todo(Long time, Object id, Member member, String content, Group group) {
        super(time, id, member);
        journal.put(time, "content", content);
        journal.put(time, "collective", group != null);
        if (group != null) {
            for (Object gm: group.members()) this.check(time, (Member)gm, false); 
        } else {
            this.check(time, member, false);            
        }
    }
    
    public Boolean check(Long time, Member member, Boolean value) {
        //System.out.println("# todo " + time + " " + member.id() + " " + value);
        return journal.put(time, member, value);
    }

    public String content() {
        return (String)journal.latest("content");
    }

    public Boolean collective() {
        return (Boolean)journal.latest("collective");
    }

    public Long submitted() {
        return journal.last(Member.class);
    }
    
    public Long submitted(Member member) {
        return journal.last(member);
    }
    
    public boolean group() {
        return false;
    }
    
    public Boolean check() {
        if (!this.collective()) {
            return (Boolean)journal.latest((Member)super.parent());
        } else {
            Object[] membs = members();
            for (int i=0; i<membs.length; i++) {
                if (!(Boolean)journal.latest(membs[i])) return false;
            }
            return true;
        }
    }

    public Object[] members() {
        return journal.all(Member.class);
    }

    public String toString() {
        Object[] membs = members();
        return super.toString(
                new Tuple("content", content()),
                new Tuple("check", check()),                
                new Tuple("collective", collective()),
                new Tuple("members", Tuple.Raw.raw(toStringMemberChecks(membs))),
                new Tuple("parent", Tuple.Raw.raw(parent()))
                );
    }
    
    public String toStringMemberChecks(Object[] membs) {
        StringBuffer sb = new StringBuffer();
        if (membs.length == 0) return "null";
        sb.append(membs.length > 1 ? "[" : "");
        for (int i=0; i<membs.length; i++) {
            String m = membs[i].toString();
            sb.append("{" + m.substring(1, m.length()-1) + ",");
            sb.append("\"check\":");
            Boolean val = (Boolean)journal.latest(membs[i]);            
            sb.append(val.toString());
            sb.append("}");
            if (i!=membs.length-1) sb.append(",");
        }
        sb.append(membs.length > 1 ? "]" : "");
        return sb.toString();
    }

}

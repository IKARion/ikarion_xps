/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.domain;

import java.util.LinkedList;

import de.ikarion.xps.base.Fact;
import de.ikarion.xps.data.Tuple;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Patch;
import name.fraser.neil.plaintext.diff_match_patch.Operation;

public abstract class Content extends Fact {
   
    public Content(Long time, Object id, Fact parent) {
        super(time, id, parent);
        journal.put(time, "value", "");
    }

    public Object[] members() {
        return journal.all(Member.class);
    }

    public Object[] patches(Member member) {
        return journal.all(member);
    }
    
    public int words() {
        String text = String.valueOf(value());
        if (text.length() < 2) return 0;
        return text.split(" ").length;
    }
    
    public int resultant(Member member) {
        return Math.max(0, inserted(member) - deleted(member));
    }
    
    public int inserted(Member member) {
        return patch(member, Operation.INSERT);
    }

    public int deleted(Member member) {
        return patch(member, Operation.DELETE);
    }
    
    public Object value() {
        StringBuffer sb = new StringBuffer();
        Object[] val = journal.get("value");
        for (Object o : val) sb.append(o);
        return sb.toString().trim();
    }
    
    public boolean value(Long time, Member member, String value) {
        String ov = String.valueOf(this.value());
        if (value == null) {
            journal.put(time, member, "");
            return false;            
        }
        String nv = value
                .replaceAll("</p>", " ")
                .replaceAll("\\<[^>]*>"," ")
                .replaceAll("&nbsp"," ")
                .replaceAll("['\"]", " ") // single and double quotes
                .replaceAll("[\\\\/]", " ") // slash und backslash
                .replaceAll("[\\\\.,:;!?]", " ") // separators
                .replaceAll("(?<!\\S)[^ ](?!\\S)", " ") // single chars
                .replaceAll("(\\s+)", " ") // redundant white spaces
                .trim();
        if (ov.equals(nv)) {
            journal.put(time, member, "");
            return false;
        }
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<Patch> patches = dmp.patch_make(ov, nv);
        for (Patch patch : patches) {
            dmp.diff_cleanupSemanticLossless(patch.diffs);
            for (Diff diff : patch.diffs) {
                if (diff.operation == Operation.INSERT) {
                    journal.put(time, member, "+" + diff.text.trim());
                } else if (diff.operation == Operation.DELETE) {
                    journal.put(time, member, "-" + diff.text.trim());
                }
            }
        }
        return journal.put(time, "value", nv);
    }
    
    public Object[] visitors() {
        return journal.all("visitors");
    }

    public boolean visitor(Long time, Member visitor) {
        return journal.put(time, "visitors", visitor);
    }

    public Long patched() {
        return journal.last(Member.class);
    }
    
    public String toString() {
        Object[] membs = members();
        return super.toString(
                new Tuple("value", value()),
                new Tuple("words", words()),
                new Tuple("members", Tuple.Raw.raw(toStringMemberPatches(membs))),
                new Tuple("visitors", Tuple.Raw.raw(visitors())),
                new Tuple("other",
                        //new Tuple("coordination", new Tuple(Tuple.Raw.raw(super.annotations("coordination")))),
                        new Tuple("classification", super.annotation("classification"))
                    ),
                Fact.verbose ? new Tuple("parent", Tuple.Raw.raw(parent())) : null
                );
    }

    private int patch(Member member, Operation operation) {
        int result = 0;
        Long[] t = journal.history(member);
        for (int j=0; j<t.length; j++) {
            Object[] p = journal.get(member, t[j]);
            for (int k=0; k<p.length; k++) {
                String mp = p[k].toString();
                if (((operation == Operation.INSERT && mp.startsWith("+")) ||
                     (operation == Operation.DELETE && mp.startsWith("-"))) && (mp.length()>2)) {
                    result += mp.split(" ").length;
                }
            }
        }
        return result;
    }
    
    public String toStringMemberPatches(Object[] membs) {
        StringBuffer sb = new StringBuffer();
        if (membs.length == 0) return "null";
        sb.append(membs.length > 1 ? "[" : "");
        for (int i=0; i<membs.length; i++) {
            String m = membs[i].toString();
            sb.append("{" + m.substring(1, m.length()-1) + ",");
            sb.append("\"patches\":");
            Long[] t = journal.history(membs[i]);
            int ins = 0;
            int del = 0;
            if (t.length != 0) sb.append(t.length > 1 ? "[" : ""); else sb.append("null");
            for (int j=0; j<t.length; j++) {
                sb.append("{\"patch\":{");
                sb.append("\"time\":" + t[j] + ",");
                sb.append("\"content\":[");
                Object[] p = journal.get(membs[i], t[j]);
                for (int k=0; k<p.length; k++) {
                    String mp = p[k].toString();
                    if (mp.length()>2) {
                        if (mp.startsWith("+")) {
                            ins += mp.split(" ").length;
                        } else if (mp.startsWith("-")) {
                            del += mp.split(" ").length;
                        }                        
                    }
                    sb.append("\"" + mp + "\"");
                    if (k!=p.length-1) sb.append(",");
                }
                sb.append("]");
                sb.append("}}");
                if (j!=t.length-1) sb.append(",");
            }
            if (t.length != 0) sb.append(t.length > 1 ? "]" : "");
            sb.append(",\"words\":{");
            sb.append("\"insert\":");
            sb.append(ins);
            sb.append(",\"delete\":");
            sb.append(del);
            sb.append("}");
            sb.append("}");
            if (i!=membs.length-1) sb.append(",");
        }
        sb.append(membs.length > 1 ? "]" : "");
        return sb.toString();
    }

}

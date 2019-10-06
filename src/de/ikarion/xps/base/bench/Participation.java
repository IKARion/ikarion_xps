/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.bench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Content;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.domain.Member;
import de.ikarion.xps.base.domain.Page;
import de.ikarion.xps.base.domain.Post;
import de.ikarion.xps.base.prompt.Prompt;
import de.ikarion.xps.data.Tuple;

public class Participation extends Prompt {

    public static double IMBALANCE_MAX = 0.5; // gini coeff. norm.

    private static final double weight_forum = 0.5;
    private static final double weight_wiki = 0.5;
    
    private ArrayList<Object> contents = null;    
    private Double base = null; 
    
    public Participation(Clock clock, Group group) {
        super(clock, group);
        this.contents = new ArrayList<Object>();
    }

    public Object[] contents() {
        return this.contents.toArray();
    }

    public void content(Object content) {
        this.base = null;
        if (!((Content)content).forgotten()) {
            this.contents.add(content);
        } else {
            this.contents.remove(content);
        }
    }
    
    private Double base() {
        
        if (this.forgotten()) return null;
        
        Group grp = (Group)this.id();
        Object[] members = grp.members();
        Object[] contents = this.contents.toArray();

        int n = members.length;
        double words[] = new double[n];
        double words_sum = 0;

        for (int i=0; i<members.length; i++) {
            Member member = (Member)members[i];
            for (int j=0; j<contents.length; j++) {
                Content content = (Content)contents[j];
                List<Object> content_members = Arrays.asList(content.members());
                if (content_members.contains(member)) {
                    double w = 0;
                    if (content instanceof Post) {
                        w = content.resultant(member) * weight_forum;
                        //w = content.inserted(member) * weight_forum;
                    } else if (content instanceof Page) {
                        w = content.resultant(member) * weight_wiki;
                        //w = content.inserted(member) * weight_wiki;
                    }
                    words[i] += w;
                    words_sum += w;
                }                    
            }
        }
        
        double avg_words = 1.0 * words_sum / n;
        double factor1 = 1.0 / (2 * avg_words);
        double factor2 = 1.0 / (n * n);
        int words_cross_sum = 0;
        
        if (avg_words == words_sum) return 0.0; // only one member in the group, thus words_cross_sum will be 0
        
        for (int i=0; i<words.length; i++) {
            for (int j=0; j<words.length; j++) {
                words_cross_sum += Math.abs(words[i] - words[j]);
            }
        }

        double gini = factor1 * factor2 * words_cross_sum;
        double gini_norm = (1.0 * n) / (n-1) * gini;

        return gini_norm;

    }
    
    public Double value() {
        if (this.base == null) this.base = this.base();
        if (this.base == null) return null;
        return ((int)(Math.max(0.0, Math.min(this.base / IMBALANCE_MAX, 1.0)) * 1000)) / 1000.0;
    }

    public String toString() {
        return super.toString(
                new Tuple("value", value()),
                new Tuple("contents", Tuple.Raw.raw(contents()))
                );
    }

}

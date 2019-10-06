/*
 * 2018, m6c7l
 */

package de.ikarion.xps.base.bench;

import java.util.ArrayList;

import de.ikarion.xps.base.domain.Clock;
import de.ikarion.xps.base.domain.Group;
import de.ikarion.xps.base.domain.Post;
import de.ikarion.xps.base.prompt.Prompt;
import de.ikarion.xps.data.Tuple;

public class Response extends Prompt {

    public static int LATENCY_MAX = 24 * 60 * 60; // seconds
    
    private ArrayList<Object> posts = null;
    private Double base = null; 
    
    public Response(Clock clock, Group group) {
        super(clock, group);
        this.posts = new ArrayList<Object>();
    }
    
    public Object[] posts() {
        return this.posts.toArray();
    }

    public void post(Object post) {
        this.base = null;
        if (!((Post)post).forgotten()) {
            this.posts.add(post);
        } else {
            this.posts.remove(post);
        }
    }

    private Double base() {
        if (this.forgotten()) return null;
        Object[] posts = this.posts.toArray();            
        if (posts.length > 0) {            
            long last = 0;
            for (int j=0; j<posts.length; j++) {
                Post post = (Post)posts[j];
                if (post.changed() > last) last = post.changed();
            }             
            return Long.valueOf(last).doubleValue();
        }
        return Double.MAX_VALUE;
    }

    public Double value() {
        if (this.base == null) this.base = this.base();
        if (this.base == null) return null;
        return ((int)(Math.max(0.0, Math.min((clock.value() - this.base) / LATENCY_MAX, 1.0)) * 1000)) / 1000.0;
    }

    public String toString() {
        return super.toString(
                new Tuple("value", value()),
                new Tuple("posts", Tuple.Raw.raw(posts()))
                );
    }

}

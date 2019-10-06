/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data.collide;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.ikarion.xps.Utility;
import de.ikarion.xps.data.Tuple;
import de.ikarion.xps.engine.Server;

public class GroupModel extends Model {

    public GroupModel(String json) throws IllegalArgumentException, IOException {
        super(json);
        if (!doc.contains(
                "work_imbalance",
                "weighted_forum_wordcount",
                "weighted_wiki_wordcount",
                "self_assessment",
                "group_sequences"))
            throw new IllegalArgumentException("malformed");
        
        //if (!this.task_id().equals(104)) return;
        
//        System.out.println("===========");
//
//        Object[] o = null;
//
//        System.out.println("id     " + model_metadata__course_id());
//        System.out.println("task   " + model_metadata_task_context__task_id() + " " + this.task_id());
//        System.out.println("course " + model_metadata_task_context__courseid());
//        System.out.println("name   " + model_metadata_task_context__task_name());
//
//        System.out.println("-----------");
//
//        Object[] rsid = task_resources_id();
//        Object[] rsty = task_resources_type();
//        for (int i=0; i<rsid.length; i++) {
//            System.out.println(rsid[i] + " " + rsty[i]);
//        }
//
//        System.out.println("-----------");
//
//        System.out.print ("model_metadata_groups__group_id ");
//        o = model_metadata_groups__group_id();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.println("-----------");
//
//        System.out.print ("work_imbalance__group_id ");
//        o = work_imbalance__group_id();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.print ("work_imbalance__gini_index ");
//        o = work_imbalance__gini_index();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.println("-----------");
//
//        System.out.print ("weighted_forum_wordcount__group_id ");
//        o = weighted_forum_wordcount__group_id();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.print ("weighted_wiki_wordcount__group_id ");
//        o = weighted_wiki_wordcount__group_id();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.print ("self_assessment__group_id ");
//        o = self_assessment__group_id();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.print ("group_sequences__group_id ");
//        o = group_sequences__group_id();
//        for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//        System.out.println("-----------");
//        
//        System.out.print ("groups ");
//        Object[] g = groups();
//        for (int i=0; i<g.length; i++) System.out.print(g[i] + " "); System.out.println();
//
//        System.out.println("model_metadata_groups__group_members "); // + model_metadata_groups__group_members(g[0]));
//        System.out.print ("users " + g[0] + " ");
//        Object[] m = users(g[0]);
//        for (int i=0; i<m.length; i++) System.out.print(m[i] + " "); System.out.println();
//        
//        if (g.length > 1) {
//            System.out.println("model_metadata_groups__group_members "); // + model_metadata_groups__group_members(g[1]));
//            System.out.print ("users " + g[1] + " ");
//            Object[] mm = users(g[1]);
//            for (int i=0; i<mm.length; i++) System.out.print(mm[i] + " "); System.out.println();            
//        }
//
//        System.out.println("-----------");
//
//        System.out.print ("group_sequences_sequence__object_id  " + g[0] + " ");
//        Object[] p = group_sequences_sequence__object_id(g[0]);
//        System.out.println();
//        for (int i=0; i<p.length; i++) System.out.println(p[i] + " "); System.out.println();
//
//        System.out.print ("group_sequences_sequence__user_id  " + g[0] + " ");
//        Object[] qp = group_sequences_sequence__user_id(g[0]);
//        System.out.println();
//        for (int i=0; i<qp.length; i++) System.out.println(qp[i] + " "); System.out.println();
//
//        if (g.length > 1) {
//            System.out.print ("group_sequences_sequence__object_id  " + g[1] + " ");
//            Object[] pp = group_sequences_sequence__object_id(g[1]);
//            System.out.println();
//            for (int i=0; i<pp.length; i++) System.out.println(pp[i] + " "); System.out.println();            
//
//            System.out.print ("group_sequences_sequence__user_id  " + g[1] + " ");
//            Object[] pq = group_sequences_sequence__user_id(g[1]);
//            System.out.println();
//            for (int i=0; i<pq.length; i++) System.out.println(pq[i] + " "); System.out.println();
//        }
//            
//        System.out.println("-----------");
//
//        if (g.length > 1) {
//            
//            System.out.print ("weighted_forum_wordcount_group_members__user_id  " + g[1] + " ");
//            o = weighted_forum_wordcount_group_members__user_id(g[1]);
//            for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//
//            System.out.print ("weighted_forum_wordcount_group_members__weighted_forum_wordcount " + g[1] + " ");
//            o = weighted_forum_wordcount_group_members__weighted_forum_wordcount(g[1]);
//            for (int i=0; i<o.length; i++) System.out.print(o[i] + " "); System.out.println();
//            
//        }
//      
//        System.out.println("===========");

    }

    private Tuple    work_imbalance()                                                             { return bag(doc, "work_imbalance"); }
    private Object[] work_imbalance__group_id()                                                   { return all(work_imbalance(), "group_id"); }    
    private Object[] work_imbalance__gini_index()                                                 { return all(work_imbalance(), "gini_index"); }    

    private Tuple    weighted_forum_wordcount()                                                   { return bag(doc, "weighted_forum_wordcount"); }
    private Object[] weighted_forum_wordcount__group_id()                                         { return all(weighted_forum_wordcount(), "group_id"); }
    private Tuple    weighted_forum_wordcount__group_members(Object gid)                          { return bag(weighted_forum_wordcount(), "group_members", Utility.find(weighted_forum_wordcount__group_id(), gid)); }              
    private Object[] weighted_forum_wordcount_group_members__user_id(Object gid)                  { return all(weighted_forum_wordcount__group_members(gid), "user_id"); }      
    private Object[] weighted_forum_wordcount_group_members__weighted_forum_wordcount(Object gid) { return all(weighted_forum_wordcount__group_members(gid), "weighted_forum_wordcount"); }      

    private Tuple    weighted_wiki_wordcount()                                                    { return bag(doc, "weighted_wiki_wordcount"); }
    private Object[] weighted_wiki_wordcount__group_id()                                          { return all(weighted_wiki_wordcount(), "group_id"); }
    private Tuple    weighted_wiki_wordcount__group_members(Object gid)                           { return bag(weighted_wiki_wordcount(), "group_members", Utility.find(weighted_wiki_wordcount__group_id(), gid)); }              
    private Object[] weighted_wiki_wordcount_group_members__user_id(Object gid)                   { return all(weighted_wiki_wordcount__group_members(gid), "user_id"); }      
    private Object[] weighted_wiki_wordcount_group_members__weighted_wiki_wordcount(Object gid)   { return all(weighted_wiki_wordcount__group_members(gid), "weighted_wiki_wordcount"); }      

    private Tuple    self_assessment()                                                            { return bag(doc, "self_assessment"); }
    private Object[] self_assessment__group_id()                                                  { return all(self_assessment(), "group_id"); }
    private Tuple    self_assessment__group_members(Object gid)                                   { return bag(self_assessment(), "group_members", Utility.find(self_assessment__group_id(), gid)); }              
    private Object[] self_assessment_group_members__user_id(Object gid)                           { return all(self_assessment__group_members(gid), "user_id"); }      
    private Object[] self_assessment_group_members__item1(Object gid)                             { return all(self_assessment__group_members(gid), "item1"); }      
    private Object[] self_assessment_group_members__item2(Object gid)                             { return all(self_assessment__group_members(gid), "item2"); }      
    private Object[] self_assessment_group_members__item3(Object gid)                             { return all(self_assessment__group_members(gid), "item3"); }      
    
    private Tuple    group_sequences()                                                            { return bag(doc, "group_sequences"); }
    private Object[] group_sequences__group_id()                                                  { return all(group_sequences(), "group_id"); }
    private Tuple    group_sequences__sequence(Object gid)                                        { return bag(group_sequences(), "sequence", Utility.find(group_sequences__group_id(), gid)); }              
    private Object[] group_sequences_sequence__object_id(Object gid)                              { return all(group_sequences__sequence(gid), "object_id"); }      
    private Object[] group_sequences_sequence__object_name(Object gid)                            { return all(group_sequences__sequence(gid), "object_name"); }      
    private Object[] group_sequences_sequence__object_type(Object gid)                            { return all(group_sequences__sequence(gid), "object_type"); }      
    private Object[] group_sequences_sequence__timestamp(Object gid)                              { return all(group_sequences__sequence(gid), "timestamp"); }      
    private Object[] group_sequences_sequence__user_id(Object gid)                                { return all(group_sequences__sequence(gid), "user_id"); }      
    private Object[] group_sequences_sequence__verb_id(Object gid)                                { return all(group_sequences__sequence(gid), "verb_id"); }      
    private Object[] group_sequences_sequence__class(Object gid)                                  { return all(group_sequences__sequence(gid), "class"); }      

    // ----------------------------------
    
    public Object gini_index(Object group_id) {
        int index = Utility.find(work_imbalance__group_id(), group_id);
        if (index > -1) return work_imbalance__gini_index()[index];            
        return null;
    }
    
    public List<List<Object>> forum_users_wordcounts(Object group_id) {
        return Utility.zip(this.forum_users(group_id), this.forum_wordcounts(group_id));
    }
    
    private Object[] forum_users(Object group_id) {
        return weighted_forum_wordcount_group_members__user_id(group_id);
    }

    private Object[] forum_wordcounts(Object group_id) {
        return weighted_forum_wordcount_group_members__weighted_forum_wordcount(group_id);
    }

    public List<List<Object>> wiki_users_wordcounts(Object group_id) {
        return Utility.zip(this.wiki_users(group_id), this.wiki_wordcounts(group_id));
    }
    
    private Object[] wiki_users(Object group_id) {
        return weighted_wiki_wordcount_group_members__user_id(group_id);
    }

    private Object[] wiki_wordcounts(Object group_id) {
        return weighted_wiki_wordcount_group_members__weighted_wiki_wordcount(group_id);
    }

    public Object[] assess_users(Object group_id) {
        return self_assessment_group_members__user_id(group_id);
    }

    public Object[] assess_items1(Object group_id) {
        return self_assessment_group_members__item1(group_id);
    }

    public Object[] assess_items2(Object group_id) {
        return self_assessment_group_members__item2(group_id);
    }

    public Object[] assess_items3(Object group_id) {
        return self_assessment_group_members__item3(group_id);
    }

    // ----------------------------------

    public List<List<Object>> sequence_ids_users_classes(Object group_id) {
        return Utility.zip(this.sequence_ids(group_id), this.sequence_users(group_id), this.sequence_classes(group_id));
    }

    // ----------------------------------

    public List<List<Object>> sequence_ids_timestamps(Object group_id) {
        return Utility.zip(this.sequence_ids(group_id), this.sequence_timestamps(group_id));
    }

    public List<List<Object>> sequence_ids_users(Object group_id) {
        return Utility.zip(this.sequence_ids(group_id), this.sequence_users(group_id));
    }
    
    public List<List<Object>> sequence_ids_classes(Object group_id) {
        return Utility.zip(this.sequence_ids(group_id), this.sequence_classes(group_id));
    }
    
    public List<List<Object>> sequence_ids_parents(Object group_id) {
        return Utility.zip(this.sequence_ids(group_id), this.sequence_parents(group_id));
    }

    public List<List<Object>> sequence_ids_modules(Object group_id) {
        return Utility.zip(this.sequence_ids(group_id), this.sequence_modules(group_id));
    }

    private Object[] sequence_ids(Object group_id) {
        Object[] obj = group_sequences_sequence__object_id(group_id);        
        for (int i=0; i<obj.length; i++) obj[i] = Utility.number(obj[i].toString()); return obj;
    }
    
    private Object[] sequence_modules(Object group_id) {
        Object[] obj = group_sequences_sequence__object_id(group_id);        
        for (int i=0; i<obj.length; i++) obj[i] = module(obj[i].toString()).getSimpleName().toLowerCase(); return obj;
    }

    private Object[] sequence_parents(Object group_id) {
        Object[] obj = group_sequences_sequence__object_id(group_id);        
        for (int i=0; i<obj.length; i++) {
            String s = obj[i].toString();
            int k = s.lastIndexOf("#"); 
            if (k > -1) {
                s = s.substring(0, k);
                obj[i] = Utility.number(s);
            } else {
                obj[i] = null;
            }
        }
        return obj;
    }
    
    private Object[] sequence_timestamps(Object group_id) {
        return group_sequences_sequence__timestamp(group_id);
    }
        
    protected Object[] sequence_types(Object group_id) {
        return group_sequences_sequence__object_type(group_id);
    }    

    protected Object[] sequence_names(Object group_id) {
        return group_sequences_sequence__object_name(group_id);
    }
        
    private Object[] sequence_users(Object group_id) {
        return group_sequences_sequence__user_id(group_id);
    }
        
    protected Object[] sequence_verbs(Object group_id) {
        return group_sequences_sequence__verb_id(group_id);
    }
        
    private Object[] sequence_classes(Object group_id) {
        return group_sequences_sequence__class(group_id);
    }

    // ----------------------------------

    public final boolean valid(Server xps) {
        Model gm = this;
        //if (Long.valueOf(gm.stamping().toString()) < xps.last()) return false;
        List<Object> objs = Arrays.asList(xps.engine().list(GroupModel.class));
        return !objs.contains(gm);
    }

}
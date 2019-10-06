/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data.collide;

import java.io.IOException;
import java.util.List;

import de.ikarion.xps.Utility;
import de.ikarion.xps.base.domain.Forum;
import de.ikarion.xps.base.domain.Module;
import de.ikarion.xps.base.domain.Wiki;
import de.ikarion.xps.data.Data;
import de.ikarion.xps.data.Tuple;
import de.ikarion.xps.engine.Server;

public abstract class Model extends Data {

    private String repr = null;
    
    public Model(String json) throws IllegalArgumentException, IOException {
        super(json);
        if (!doc.contains("model_metadata"))
            throw new IllegalArgumentException("invalid");
        if (!doc.pick("model_metadata").contains("course_id", "task_context", "groups"))
            throw new IllegalArgumentException("malformed");
        this.repr = doc.all().value("model").toString();
    }

    protected Tuple    model_metadata()                                          { return bag(doc, "model_metadata"); }
    protected Object   model_metadata__course_id()                               { return get(model_metadata(), "course_id"); }
    
    protected Tuple    model_metadata__task_context()                            { return bag(model_metadata(), "task_context"); }    
    protected Object   model_metadata_task_context__courseid()                   { return get(model_metadata__task_context(), "courseid"); }
    protected Object   model_metadata_task_context__task_name()                  { return get(model_metadata__task_context(), "task_name"); }
    protected Object   model_metadata_task_context__task_id()                    { return get(model_metadata__task_context(), "task_id"); }    
    protected Object   model_metadata_task_context__task_start()                 { return get(model_metadata__task_context(), "task_start"); }
    protected Object   model_metadata_task_context__task_end()                   { return get(model_metadata__task_context(), "task_end"); }
    protected Object   model_metadata_task_context__task_type()                  { return get(model_metadata__task_context(), "task_type"); }
    
    protected Tuple    model_metadata_task_context__task_resources()             { return bag(model_metadata__task_context(), "task_resources"); }
    
    protected Tuple    model_metadata__groups()                                  { return bag(model_metadata(), "groups"); }
    protected Object[] model_metadata_groups__group_id()                         { return all(model_metadata__groups(), "group_id"); }
    protected Tuple    model_metadata_groups__group_members(Object gid)          { return bag(model_metadata__groups(), "group_members", Utility.find(model_metadata_groups__group_id(), gid)); }              
    protected Object[] model_metadata_groups_group_members__email(Object gid)    { return all(model_metadata_groups__group_members(gid), "email"); }      
    protected Object[] model_metadata_groups_group_members__fullname(Object gid) { return all(model_metadata_groups__group_members(gid), "fullname"); }      
    protected Object[] model_metadata_groups_group_members__name(Object gid)     { return all(model_metadata_groups__group_members(gid), "name"); }      
    protected Object[] model_metadata_groups_group_members__username(Object gid) { return all(model_metadata_groups__group_members(gid), "username"); }      
    
    // ----------------------------------

    public Object course() {
        return Utility.number(model_metadata__course_id().toString());
    }

    public Object task_course() {
        return Utility.number(model_metadata_task_context__courseid().toString());
    }

    public Object task_id() {
        return Utility.number(model_metadata_task_context__task_id().toString());
    }

    public Object task_name() {
        return model_metadata_task_context__task_name();
    }

    public Object task_from() {
        return Utility.number(model_metadata_task_context__task_start().toString());
    }

    public Object task_to() {
        return Utility.number(model_metadata_task_context__task_end().toString());
    }

    public Object task_type() {
        return model_metadata_task_context__task_type();
    }

    public List<List<Object>> task_resources_id_type() {
        return Utility.zip(this.task_resources_id(), this.task_resources_type());
    }
    
    protected Object[] task_resources_id() {        
        Tuple tup = model_metadata_task_context__task_resources();
        int n = 0; if (tup.list()) n = tup.size(); else if (tup.single()) n = 1;     
        Object[] obj = new Object[n];
        if (n > 1) for (int i=0; i<n; i++) obj[i] = Utility.number(tup.get(i).value().toString());
        else if (n == 1) obj[0] = Utility.number(tup.value().toString());
        return obj;
    }

    protected Object[] task_resources_type() {
        Tuple tup = model_metadata_task_context__task_resources();        
        int n = 0; if (tup.list()) n = tup.size(); else if (tup.single()) n = 1;     
        Object[] obj = new Object[n];
        if (n > 1) for (int i=0; i<n; i++) obj[i] = module(tup.get(i).value().toString()).getSimpleName().toLowerCase();            
        else if (n == 1) obj[0] = module(tup.value().toString()).getSimpleName().toLowerCase();        
        return obj;
    }

    public Object[] groups() {
        return model_metadata_groups__group_id();
    }

    public Object[] member_email(Object group_id) {     
        return model_metadata_groups_group_members__email(group_id);
    }

    public Object[] member_fullname(Object group_id) {
        return model_metadata_groups_group_members__fullname(group_id);
    }

    public Object[] member_name(Object group_id) { // == user_id !!!
        return model_metadata_groups_group_members__name(group_id);
    }
    
    public Object[] member_username(Object group_id) {
        return model_metadata_groups_group_members__username(group_id);
    }
    
    public Object[] users(Object group_id) {
        return this.member_name(group_id);
    }
    
    // ----------------------------------

    protected static Class<Module> module(String url) {
        Class<Module>[] domain = new Class[] {Forum.class, Wiki.class};
        for (int i=0; i<domain.length; i++) if (url.contains("/" + domain[i].getSimpleName().toLowerCase() + "/")) return domain[i];
        return null;
    }
    
    // ----------------------------------
    
    public abstract boolean valid(Server xps);
    
    public String toString() {
        String s = repr();
        s = s.substring(0,  s.length() - 1);
        return String.format("%s", s);
    }

    private String repr() {
        return this.repr;
    }
    
    public boolean equals(Object o) {
        if ((o==null) || (!(o instanceof Model))) return false;
        return this.repr().equals(((Model)o).repr());
    }

}
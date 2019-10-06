/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data.moodle;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.ikarion.xps.data.Data;
import de.ikarion.xps.data.Tuple;
import de.ikarion.xps.engine.Server;

public final class Event extends Data {
   
    private String repr = null;
    
    public Event(String json) throws IllegalArgumentException, IOException {
        super(json);
        if (!doc.contains("eventname", "component", "action", "target"))
            throw new IllegalArgumentException("invalid");
        this.repr = doc.all().value("event").toString();
    }

    public  Object   component()         { return get(doc, "component"); }
    public  Object   action()            { return get(doc, "action"); }
    public  Object   target()            { return get(doc, "target"); }
    public  Object   objectid()          { return get(doc, "objectid"); }
    public  Object   crud()              { return get(doc, "crud"); }
    public  Object   contextid()         { return get(doc, "contextid"); }
    public  Object   contextlevel()      { return get(doc, "contextlevel"); }
    public  Object   contextinstanceid() { return get(doc, "contextinstanceid"); }
    public  Object   userid()            { return get(doc, "userid"); }
    public  Object   courseid()          { return get(doc, "courseid"); }
    public  Object   relateduserid()     { return get(doc, "relateduserid"); }
    public  Tuple    other()             { return bag(doc, "other"); }
    public  Object   timecreated()       { return get(doc, "timecreated"); }
    
    private Tuple    other(String key)   { return bag(other(), key); }

    public  Object   content()           { return get(other(), "content"); }
    public  Object   newcontent()        { return get(other(), "newcontent"); }
    public  Object   discussionid()      { return get(other(), "discussionid"); }
    public  Object   forumid()           { return get(other(), "forumid"); }
    
    public  Object   instanceid()        { return get(other(), "instanceid"); }
    public  Object   groupid()           { return get(other(), "groupid"); }
    public  Object   modulename()        { return get(other(), "modulename"); }
    public  Object   toforumid()         { return get(other(), "toforumid"); }
    
    public  Tuple    _items()            { return other("items"); }
    
    public  Object   _id()               { return get(other(), "id"); }
    public  Object   _title()            { return get(other(), "title"); }
    public  Object   _groupmode()        { return get(other(), "groupmode"); }
    public  Object   _value()            { return get(other(), "value"); }
    public  Object   _name()             { return get(other(), "name"); }

    public  Tuple    _dates()            { return other("dates"); }
    public  Object   _tz()               { return get(other(), "tz"); }
    public  Object   _st()               { return get(other(), "st"); }
    
//    public  Tuple    _mon()              { return bag(other(), "mon"); }
//    public  Tuple    _tue()              { return bag(other(), "tue"); }
//    public  Tuple    _wed()              { return bag(other(), "wed"); }
//    public  Tuple    _thu()              { return bag(other(), "thu"); }
//    public  Tuple    _fri()              { return bag(other(), "fri"); }
//    public  Tuple    _sat()              { return bag(other(), "sat"); }
//    public  Tuple    _sun()              { return bag(other(), "sun"); }
    
    public String toString() {
        String s = repr();
        s = s.substring(0,  s.length() - 1);
        return String.format("%s", s);
    }

    private String repr() {
        return this.repr;
    }
    
    public boolean equals(Object o) {
        if ((o==null) || (!(o instanceof Event))) return false;
        return this.repr().equals(((Event)o).repr());
    }
    
    public final boolean valid(Server xps) {
        Event ev = this;
        //if (Long.valueOf(ev.stamping().toString()) < xps.last()) return false; 
        List<Object> objs = Arrays.asList(xps.engine().list(Event.class));
        return !objs.contains(ev);
    }
    
}
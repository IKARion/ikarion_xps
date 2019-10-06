/*
 * 2018, m6c7l
 */

package de.ikarion.xps.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.drools.core.ClassObjectFilter;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.TimedRuleExecutionOption;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.io.ResourceFactory;

import de.ikarion.xps.base.Fact;

public class Engine {
    
    public static final String WATCHDOG_RULE = "common:watchdog";
    public static final int INFERENCE_TIMEOUT = 4;
    
    private KieSession ksess = null;
    private KieSessionConfiguration ksesscfg = null;
    private KieBase kbase = null;
    //private AgendaEventListener ksessael = null;
    //private SessionPseudoClock clock = null;
    
    public Engine(File[] rules) {
        
        KieServices kserv = KieServices.Factory.get();
        
        // Adds the drools file to the KieFileSystem for necessary compilation to occur
        KieFileSystem kfs = kserv.newKieFileSystem();
        for (File rule : rules) kfs.write(ResourceFactory.newFileResource(rule));
        
        // Create the builder for the resources of the File System
        KieBuilder kb = kserv.newKieBuilder(kfs);
        kb.buildAll();
        
        // Create the Container, wrapping the KieModule with the given ReleaseId
        ReleaseId rid = kb.getKieModule().getReleaseId();
        KieContainer kcont = kserv.newKieContainer(rid);
        
        // Configure and create a KieContainer that reads the drools files
        KieBaseConfiguration conf = kserv.newKieBaseConfiguration();
        //conf.setProperty("drools.dialect.mvel.strict", "DISABLED");
        //conf.setProperty("drools.propertySpecific", PropertySpecificOption.ALLOWED.toString());
        
        conf.setOption(EventProcessingOption.CLOUD);
        //conf.setOption(EventProcessingOption.STREAM);
        
        this.kbase = kcont.newKieBase(conf);
        
        // Configure and create the KieSession
        this.ksesscfg = kserv.newKieSessionConfiguration();
        this.ksesscfg.setOption(TimedRuleExecutionOption.YES);
        //this.ksesscfg.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
        //this.ksesscfg.setProperty("drools.propertySpecific", PropertySpecificOption.ALLOWED.toString());
        
        this.ksess = this.kbase.newKieSession(this.ksesscfg, null);
        //this.clock = this.ksess.getSessionClock();
        
        // Put logging into being
        //this.ksessael = new AgendaLogger(null);
        //this.ksess.addEventListener(this.ksessael);

    }
    
    public Object[] query(String query, Object[] arguments) {
        
        Fact.verbose = arguments[1] == null; // id set?
        
        ArrayList<Object> result = new ArrayList<Object>();
        try {
            QueryResults results = this.ksess.getQueryResults(query, arguments);
            for (QueryResultsRow row : results) {
                result.add(row.get("result"));
            }
        } catch (Exception e) {
            return null;
        }
        return result.toArray();
    }
    

    public Object[] list() {
        return this.list(Object.class);
    }

    public Object[] list(Class<?> value) {
        Collection<?> coll = this.ksess.getObjects(new ClassObjectFilter(value));
        Object[] result = coll.toArray();
        try { Arrays.sort(result); } catch (Exception e) {}
        return result;
    }
    
    public long size() {
        return this.ksess.getFactCount();
    }
    
    public void put(Object... facts) {
        for (Object fact : facts) {
            this.ksess.insert(fact);
        }
    }
    
    public void remove(Object... facts) {
        for (Object fact : facts) {
            FactHandle fh = this.ksess.getFactHandle(fact);
            this.ksess.delete(fh);
        }
    }
    
    public void halt() {
        this.ksess.halt();
    }
    
    public void reset() {
        this.ksess.dispose();
        this.ksess = this.kbase.newKieSession(this.ksesscfg, null);
        //((AgendaLogger)this.ksessael).reset();
        //this.ksess.addEventListener(this.ksessael);
    }

    public Inference infer() {
        return this.infer("");
    }

    public Inference infer(final String prefix) {
        return this.infer(new String[] {"MAIN"}, prefix);
    }

    public Inference infer(final String[] groups) {
        return this.infer(groups, "");
    }

    public Inference infer(final String[] groups, final String prefix) {        
        Inference result = new Inference();
        this.ksess.setGlobal("g", result);
        for (int i=0; i<groups.length; i++) {
            this.ksess.getAgenda().getAgendaGroup(groups[i]).setFocus();
            this.ksess.fireAllRules(new AgendaFilter() {
                public boolean accept(Match match) {
                    if (Pattern.matches(prefix + ".{1,}", match.getRule().getName())) {
                        return true;
                    }
                    return false;
                }});            
        }
        return result; //this.ksess.getGlobal("g");
    }
    
//    public String log() {
//        return ((AgendaLogger)this.ksessael).toString();
//    }

}

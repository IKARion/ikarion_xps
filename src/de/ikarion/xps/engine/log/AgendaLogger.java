package de.ikarion.xps.engine.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.rule.Match;

import de.ikarion.xps.data.Data;
import de.ikarion.xps.data.Tuple;
import de.ikarion.xps.engine.Engine;

public class AgendaLogger extends DefaultAgendaEventListener  {

    private Tuple log = null;
    private List<Match> matchList = new ArrayList<Match>();
    private Engine engine = null;
    
    public AgendaLogger(Engine engine) {
        this.log = new Tuple(this.getClass().getSimpleName());
        this.engine = engine;
    }
    
    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {

        if (this.log.single()) {
            this.log.add(System.currentTimeMillis());
        }

        Rule rule = event.getMatch().getRule();
        String ruleName = rule.getName();

        if ((engine != null) && (ruleName.equals(Engine.WATCHDOG_RULE))) {
            Object[] objs = engine.list(Data.class);
            for (int i=0; i<objs.length; i++) {
                Data data = (Data)objs[i];
                data.discard();
            }
            engine.halt();
        }
        
        this.matchList.add(event.getMatch());

        Map<String, Object> ruleMetaDataMap = rule.getMetaData();
        Tuple entry = new Tuple(ruleName, System.currentTimeMillis() - (long)(this.log.first().value()));     
        if (ruleMetaDataMap.size() > 0) {
            Tuple meta = new Tuple(ruleMetaDataMap.size());
            for (String key : ruleMetaDataMap.keySet()) {
                meta.add(key, ruleMetaDataMap.get(key));
            }
            entry.add(meta);
        }
        
        this.log.add(entry);
        
    }

    public boolean isRuleFired(String ruleName) {
        for (Match match : this.matchList) {
            if (match.getRule().getName().equals(ruleName)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        this.matchList.clear();
        this.log = new Tuple(this.getClass());
    }

    public final List<Match> getMatchList() {
        return this.matchList;
    }

    public String toString() {
        return this.log.toString();
    }

}
/*
 * 2018, m6c7l
 */

package de.ikarion.xps;

import java.io.File;
import java.io.IOException;

import de.ikarion.xps.engine.Engine;
import de.ikarion.xps.engine.Server;
import de.ikarion.xps.engine.test.Test;
import de.ikarion.xps.rpc.RpcServer;

public class Start {
        
    public static void main(String[] args) {
        
        Engine xps = new Engine(new File[] {

                new File("res/rules/common.drl"),

                new File("res/rules/event.drl"),
                new File("res/rules/awareness.drl"),
                new File("res/rules/model.drl"),

                new File("res/rules/prompt.drl"),
                new File("res/rules/progress.drl"),

                new File("res/query/domain.drl"),
                new File("res/query/monitoring.drl"),

                // ---

                new File("res/test/common.drl"),
                new File("res/test/fibonacci.drl"),
                new File("res/test/threesons.drl"),
                new File("res/test/location.drl"),

        });
        
        try {
            
            System.err.println("< Begin > Tests");
            Test.doTestFibonacci(xps);
            Test.doTestThreeSons(xps);
            Test.doTestLocation(xps);
            System.err.println("< End > Tests");
            
        } catch (RuntimeException e) {
            System.err.println("[fail] XPS service - - " + e);
            System.exit(1);
        }
        
        System.err.println("[ OK ] XPS service");

        try {
            
            RpcServer server = new RpcServer(50100);
            server.addHandler("xps", new Server(xps, args));
            server.serve_forever();

            System.err.println("[ OK ] RPC service");
            
        } catch (IOException e) {
            
            System.err.println("[fail] RPC service - - " + e);
            System.exit(1);
            
        }

    }

}

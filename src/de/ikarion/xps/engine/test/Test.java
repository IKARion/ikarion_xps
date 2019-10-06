package de.ikarion.xps.engine.test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.ikarion.xps.data.Tuple;
import de.ikarion.xps.engine.Engine;
import de.ikarion.xps.engine.Inference;
import de.ikarion.xps.engine.test.fibonacci.Number;
import de.ikarion.xps.engine.test.location.Location;
import de.ikarion.xps.engine.test.threesons.Age;

public final class Test {

    private Test() { }
    
    public static void doTestLocation(Engine xps) throws RuntimeException {
        System.err.println("< Test > decision checking (backward chaining)");
        Location l1 = new Location("Europe", "World");
        Location l2 = new Location("France", "Europe");
        Location l3 = new Location("Paris", "France");
        Location l4 = new Location("Eiffel tower", "Paris");
        xps.put(l1, l2, l3, l4);
        Inference result = xps.infer("location");
        xps.reset();
        if (!result.contains("Eiffel tower is in the world.")) {
            throw new RuntimeException("Test Location failed.");
        }
    }

    public static void doTestFibonacci(Engine xps) throws RuntimeException {
        System.err.println("< Test > recursion");
        Number f = new Number(80);
        xps.put(f);
        Inference result = xps.infer("fibonacci");
        Object[] obj = result.all();
        xps.reset();
        if (!("{\"80\":23416728348467685}".equals(String.valueOf(obj[obj.length - 1])))) {
            throw new RuntimeException("Test Fibonacci failed.");            
        }
    }
    
    public static void doTestThreeSons(Engine xps) throws RuntimeException {
        System.err.println("< Test > complex inference (forward chaining)");
        for (int j = 1; j <= 36; j++) {
            Age age = new Age(j);
            xps.put(age);
        }        
        Inference result = xps.infer("threesons");
        Object[] obj = result.all();
        xps.reset();
        if (!"[ 2, 2, 9], (product: 36, sum: 13)".equals(String.valueOf(obj[0]))) {
            throw new RuntimeException("Test ThreeSons failed.");
        }
    }
    
    public static void doTestPojo() {
        
        try {
            
            Map<String, Class<?>> props = new HashMap<String, Class<?>>();
            props.put("foo", Integer.class);
            props.put("bar", String.class);
          
            Class<?> clazz = Pojo.generate("de.ikarion.xps.data.Fact", props);
            Object obj = clazz.newInstance();
          
            System.out.println("class       \t" + clazz);
            System.out.println("object      \t" + obj);
            System.out.println("serializable\t" + (obj instanceof Serializable));
          
            for (final Method method : clazz.getDeclaredMethods()) {
                System.out.println("method     \t" + method);
            }
          
            clazz.getMethod("setBar", String.class).invoke(obj, "Hello, World.");
            String result = (String) clazz.getMethod("getBar").invoke(obj);            
            System.out.println("value @ bar\t" + result);
          
            System.out.println("bar      \t" + ((Pojo.PojoImpl)obj).getBar());
            System.out.println("foo      \t" + ((Pojo.PojoImpl)obj).getFoo());
                      
        } catch (Exception e) {
            System.out.println(e);
        }
        
        System.out.println();
        
    }

    public static void doTestTuples() {
        Tuple v = new Tuple(4, 5, 6);
        Tuple u = new Tuple(3, v);
        Tuple t = new Tuple(1, 2, u);
        boolean[] res = new boolean[] {
                t.has(1),
                t.has(2, 1),
                t.has(3, 2, 1),
                t.has(u),
                t.has(u, 2),
                t.has(u, 2, 1),
                !t.has(4),
        };        
        System.out.println(t + "\n" + (res[0]==res[1]==res[2]==res[3]==res[4]==res[5]==res[6]) + "\n" + t.flat());
        System.out.println();
    }
    
}

/*
 * 2018, m6c7l
 */

package de.ikarion.xps.data;

import java.io.IOException;
import java.util.Map;

import de.ikarion.xps.Utility;

public class Doc extends Tuple {

    public Doc(String id, Map<String, Class<?>> keys, Object... values) {        
        super(id);
        int count = 0;
        for (Map.Entry<String, Class<?>> arg : keys.entrySet()) {
            if (values.length == count) break;
            super.add(arg.getKey(), (arg.getValue().cast(values[count])));
            count++;
        }                        
    }

    public Doc(String json) throws IOException {
        super(System.currentTimeMillis() / 1000);
        this.add(Utility.json(json).get());
    }
    
}

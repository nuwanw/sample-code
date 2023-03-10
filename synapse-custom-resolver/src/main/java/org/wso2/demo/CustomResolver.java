package org.wso2.demo;

import org.apache.synapse.commons.resolvers.Resolver;

import java.util.HashMap;
import java.util.Map;

public class CustomResolver implements Resolver {
    //putting sample values
    private static Map<String, String> values = new HashMap<>() {{
        put("x", "a");
        put("y", "b");
    }};
    //private static Map<String, String> values = Map.of("x", "a", "y", "b");
    private String input;

    @Override
    public void setVariable(String s) {
        this.input = s;
    }

    @Override
    public String resolve() {
        return values.get(input);
    }
}

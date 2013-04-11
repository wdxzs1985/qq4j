package org.qq4j.core;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class QQSession {

    private final Map<String, Object> mapping;

    public QQSession() {
        final Map<String, Object> m = new WeakHashMap<String, Object>();
        this.mapping = Collections.synchronizedMap(m);
    }

    public Object put(final String key, final Object obj) {
        return this.mapping.put(key, obj);
    }

    public Object get(final String key) {
        return this.mapping.get(key);
    }

    public void remove(final String key) {
        this.mapping.remove(key);
    }
}

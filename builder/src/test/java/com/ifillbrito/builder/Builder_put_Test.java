package com.ifillbrito.builder;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class Builder_put_Test
{
    @Test
    public void put()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setStringIntegerMap, new HashMap<>())
                .put(ObjectA::getStringIntegerMap, "key", 1)
                .build();

        assertEquals((Integer) 1, objectA.getStringIntegerMap().get("key"));
    }

    @Test
    public void putAll()
    {
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("key1", 1);
        inputMap.put("key2", 2);
        inputMap.put("key3", 3);

        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setStringIntegerMap, new HashMap<>())
                .putAll(ObjectA::getStringIntegerMap, inputMap)
                .build();

        assertEquals((Integer) 1, objectA.getStringIntegerMap().get("key1"));
        assertEquals((Integer) 2, objectA.getStringIntegerMap().get("key2"));
        assertEquals((Integer) 3, objectA.getStringIntegerMap().get("key3"));
    }

    @Test(expected = NullPointerException.class)
    public void putAll_nullMap_exception()
    {
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("key1", 1);
        inputMap.put("key2", 2);
        inputMap.put("key3", 3);

        new Builder<>(new ObjectA())
                .putAll(ObjectA::getStringIntegerMap, inputMap)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void putAll_nullGetter_exception()
    {
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("key1", 1);
        inputMap.put("key2", 2);
        inputMap.put("key3", 3);

        new Builder<>(new ObjectA())
                .putAll(null, inputMap)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_nullGetter_exception()
    {
        new Builder<>(new ObjectA())
                .set(ObjectA::setStringIntegerMap, new HashMap<>())
                .put(null, "key", 1)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void put_toNullMap_exception()
    {
        // The map in ObjectA is not initialized.
        new Builder<>(new ObjectA())
                .put(ObjectA::getStringIntegerMap, "key", 1)
                .build();
    }
}
package com.ifillbrito.builder;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_put
{
    @Test
    public void put()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setMap, new HashMap<>())
                .put(ObjectA::getMap, "key", 1)
                .build();

        assertEquals((Integer) 1, objectA.getMap().get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_nullGetter_exception()
    {
        new Builder<>(new ObjectA())
                .set(ObjectA::setMap, new HashMap<>())
                .put(null, "key", 1)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void put_toNullMap_exception()
    {
        // The map in ObjectA is not initialized.
        new Builder<>(new ObjectA())
                .put(ObjectA::getMap, "key", 1)
                .build();
    }
}
package com.ifillbrito.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by gjib on 25.01.18.
 */
public class Builder_set_Test
{
    @Test
    public void set()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setText, "text")
                .set(ObjectA::setNumber, 10)
                .build();

        assertEquals("text", objectA.getText());
        assertEquals(10, objectA.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_nullSetter_exception()
    {
        new Builder<>(new ObjectA())
                .set(null, "text")
                .build();
    }

    @Test
    public void set_nullValue()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setText, null)
                .set(ObjectA::setNumber, 10)
                .build();

        assertNull(objectA.getText());
        assertEquals(10, objectA.getNumber());
    }
}
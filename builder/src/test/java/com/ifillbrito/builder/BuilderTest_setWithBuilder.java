package com.ifillbrito.builder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_setWithBuilder
{
    @Test
    public void setWithBuilder()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class) // use this to tell the compiler the parent type
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .set(ObjectA::setText, "object A")
                    .toParent() // use this if the parent type is the same as the child type
                .build();
        //@formatter:on

        assertEquals("object A", objectA.getObjectA().getText());
        assertEquals("object B", objectA.getObjectB().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithBuilder_nullSetter_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(null, new Builder<>(new ObjectB()))
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test
    public void setWithBuilder_function()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setText, new Builder<>(new ObjectB()), ObjectB::getText)
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        assertEquals("object B", objectA.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithBuilder_nullFunction_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setText, new Builder<>(new ObjectB()), null)
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setWithBuilder_noParent_exception()
    {
        // This builder has no parent.
        new Builder<>(new ObjectA()).toParent();
    }
}
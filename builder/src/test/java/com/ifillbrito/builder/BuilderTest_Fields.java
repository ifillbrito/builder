package com.ifillbrito.builder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_Fields
{

    @Test
    public void set()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setText, "text")
                .set(ObjectA::setNumber, 10)
                .build();

        assertEquals("text", objectA.getText());
        assertEquals(10, objectA.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_nullSetter_exception()
    {
        Builder.of(new ObjectA())
                .set(null, "text")
                .build();
    }

    @Test
    public void set_nullValue()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setText, null)
                .set(ObjectA::setNumber, 10)
                .build();

        assertNull(objectA.getText());
        assertEquals(10, objectA.getNumber());
    }

    @Test
    public void setWithAlias()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectA, "alias")
                .build();

        // check reference
        assertTrue(objectA == objectA.getObjectA());
        // check contents
        assertEquals(objectA.getText(), objectA.getObjectA().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithAlias_nullSetter()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(null, "alias")
                .build();
    }

    @Test
    public void setWithAlias_nullAlias_positive()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as(null)
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectA, null)
                .build();

        // check reference
        assertTrue(objectA == objectA.getObjectA());
        // check contents
        assertEquals(objectA.getText(), objectA.getObjectA().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithAlias_invalidAlias_exception()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectA, "another alias")
                .build();
    }

    @Test(expected = ClassCastException.class)
    public void setWithAlias_classCastException()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectB, "alias")
                .build();
    }

    @Test
    public void setWithAlias_aliasAndFunction()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setWithAlias( // create ObjectB using data from ObjectA
                        ObjectA::setObjectB,
                        ObjectA.class,
                        "a",
                        a -> {
                            ObjectB b = new ObjectB();
                            b.setText(a.getText());
                            return b;
                        })
                .build();

        // check contents
        assertEquals(objectA.getText(), objectA.getObjectB().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithAlias_nullSetter_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setWithAlias( // create ObjectB using data from ObjectA
                        null,
                        ObjectA.class,
                        "a",
                        a -> {
                            ObjectB b = new ObjectB();
                            b.setText(a.getText());
                            return b;
                        })
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithAlias_nullAliasFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setWithAlias( // create ObjectB using data from ObjectA
                        ObjectA::setObjectB,
                        ObjectA.class,
                        "a",
                        null)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWithAlias_nullFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setWithAlias( // create ObjectB using data from ObjectA
                        ObjectA::setObjectB,
                        ObjectA.class,
                        "b",
                        null)
                .build();
    }

    @Test(expected = ClassCastException.class)
    public void setWithAlias_aliasAndFunction_classCastException()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setWithAlias( // create ObjectB using data from ObjectA
                        ObjectA::setObjectB,
                        ObjectB.class, // the alias "a" doesn't point to an object of type ObjectB
                        "a",
                        a -> {
                            ObjectB b = new ObjectB();
                            b.setText(a.getText());
                            return b;
                        })
                .build();
    }

    @Test
    public void setWithBuilder()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class) // use this to tell the compiler the parent type
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
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
        Builder.of(new ObjectA())
                .setWithBuilder(null, Builder.of(new ObjectB()))
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test
    public void setWithBuilder_function()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setText, Builder.of(new ObjectB()), ObjectB::getText)
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
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setText, Builder.of(new ObjectB()), null)
                    .set(ObjectB::setText, "object B")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setWithBuilder_noParent_exception()
    {
        // This builder has no parent.
        Builder.of(new ObjectA()).toParent();
    }
}
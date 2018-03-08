package com.github.ifillbrito.builder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gjib on 25.01.18.
 */
public class Builder_setWithAlias_Test
{
    @Test
    public void setWithAlias()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
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
    public void setWithAlias_nullSetter_exception()
    {
        new Builder<>(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(null, "alias")
                .build();
    }

    @Test
    public void setWithAlias_nullAlias()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
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
        new Builder<>(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectA, "another alias")
                .build();
    }

    @Test(expected = ClassCastException.class)
    public void setWithAlias_classCastException()
    {
        new Builder<>(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectB, "alias")
                .build();
    }

    @Test
    public void setWithAlias_aliasAndFunction()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
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
    public void setWithAlias_nullSetter2_exception()
    {
        new Builder<>(new ObjectA())
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
        new Builder<>(new ObjectA())
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
        new Builder<>(new ObjectA())
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
        new Builder<>(new ObjectA())
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
}
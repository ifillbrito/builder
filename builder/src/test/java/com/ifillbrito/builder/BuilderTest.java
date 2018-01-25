package com.ifillbrito.builder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest
{

    @Test
    public void set_setterAndValue()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setText, "text")
                .set(ObjectA::setNumber, 10)
                .build();

        assertEquals("text", objectA.getText());
        assertEquals(10, objectA.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_nullSetterAndValue_exception()
    {
        Builder.of(new ObjectA())
                .set(null, "text")
                .build();
    }

    @Test
    public void set_setterAndNullValue()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setText, null)
                .set(ObjectA::setNumber, 10)
                .build();

        assertNull(objectA.getText());
        assertEquals(10, objectA.getNumber());
    }

    @Test
    public void set_setterAndArgumentAndFunction()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setText, 10, String::valueOf)
                .set(ObjectA::setNumber, "10", Integer::parseInt)
                .build();

        assertEquals("10", objectA.getText());
        assertEquals(10, objectA.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_nullSetterAndArgumentAndFunction_exception()
    {
        Builder.of(new ObjectA())
                .set(null, 10, String::valueOf)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void set_setterAndArgumentAndNullFunction_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setText, 10, null)
                .build();
    }

    @Test
    public void setUsingAlias_setterAndAlias()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setUsingAlias(ObjectA::setObjectA, "alias")
                .build();

        // check reference
        assertTrue(objectA == objectA.getObjectA());
        // check contents
        assertEquals(objectA.getText(), objectA.getObjectA().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setUsingAlias_nullSetterAndAlias()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setUsingAlias(null, "alias")
                .build();
    }

    @Test
    public void setUsingAlias_setterAndNullAlias_positive()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as(null)
                .set(ObjectA::setText, "text")
                .setUsingAlias(ObjectA::setObjectA, null)
                .build();

        // check reference
        assertTrue(objectA == objectA.getObjectA());
        // check contents
        assertEquals(objectA.getText(), objectA.getObjectA().getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setUsingAlias_setterAndNonDefinedAlias_exception()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setUsingAlias(ObjectA::setObjectA, "another alias")
                .build();
    }

    @Test(expected = ClassCastException.class)
    public void setUsingAlias_setterAndAlias_classCastException()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setUsingAlias(ObjectA::setObjectB, "alias")
                .build();
    }

    @Test
    public void setUsingAlias_setterAndAliasTypeAndAliasAndFunction()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setUsingAlias( // create ObjectB using data from ObjectA
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
    public void setUsingAlias_nullSetterAndAliasTypeAndAliasAndFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setUsingAlias( // create ObjectB using data from ObjectA
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
    public void setUsingAlias_setterAndAliasTypeAndAliasAndNullFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setUsingAlias( // create ObjectB using data from ObjectA
                        ObjectA::setObjectB,
                        ObjectA.class,
                        "a",
                        null)
                .build();
    }

    @Test(expected = ClassCastException.class)
    public void setUsingAlias_setterAndAliasTypeAndAliasAndFunction_classCastException()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "text")
                .setUsingAlias( // create ObjectB using data from ObjectA
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
    public void setUsingBuilder()
    {
    }

    @Test
    public void setUsingBuilder1()
    {
    }

    @Test
    public void add()
    {
    }

    @Test
    public void add1()
    {
    }

    @Test
    public void addAll()
    {
    }

    @Test
    public void addAll1()
    {
    }

    @Test
    public void addUsingAlias()
    {
    }

    @Test
    public void addUsingAlias1()
    {
    }

    @Test
    public void addUsingBuilder()
    {
    }

    @Test
    public void addUsingBuilder1()
    {
    }

    @Test
    public void put()
    {
    }

    @Test
    public void putUsingAliases()
    {
    }

    @Test
    public void putUsingAliasForKey()
    {
    }

    @Test
    public void putUsingAliasForValue()
    {
    }

    @Test
    public void put1()
    {
    }

    @Test
    public void putUsingAliases1()
    {
    }

    @Test
    public void putUsingAliasForKey1()
    {
    }

    @Test
    public void putUsingAliasForValue1()
    {
    }

    @Test
    public void put2()
    {
    }

    @Test
    public void putUsingAliases2()
    {
    }

    @Test
    public void putUsingAliasForKey2()
    {
    }

    @Test
    public void putUsingAliasForValue2()
    {
    }

    @Test
    public void put3()
    {
    }

    @Test
    public void putUsingAliases3()
    {
    }

    @Test
    public void putUsingAliasForKey3()
    {
    }

    @Test
    public void putUsingAliasForValue3()
    {
    }

    @Test
    public void putAll()
    {
    }

    @Test
    public void putUsingBuilder()
    {
    }

    @Test
    public void putUsingAliasForKeyAndBuilderForValue()
    {
    }

    @Test
    public void putUsingBuilder1()
    {
    }

    @Test
    public void putUsingAliasForKeyAndBuilderForValue1()
    {
    }

    @Test
    public void putUsingBuilder2()
    {
    }

    @Test
    public void putUsingAliasForKeyAndBuilderForValue2()
    {
    }

    @Test
    public void put4()
    {
    }

    @Test
    public void putUsingAliasForKeyAndBuilderForValue3()
    {
    }

    @Test
    public void as()
    {
    }

    @Test
    public void toParent()
    {
    }

    @Test
    public void getAliasMap()
    {
    }
}
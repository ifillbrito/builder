package com.ifillbrito.builder;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_putWithBuilder
{
    @Test
    public void putWithBuilder()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .putWithBuilder(ObjectA::getStringObjectAMap, "objectA-1", new Builder<>(new ObjectA()))
                    .as("objectA-1")
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .putWithBuilder(ObjectA::getStringObjectAMap, "objectA-2", new Builder<>(new ObjectA()))
                    .as("objectA-2")
                    .set(ObjectA::setNumber, 2)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        assertEquals(1, objectA.getStringObjectAMap().get("objectA-1").getNumber());
        assertEquals(2, objectA.getStringObjectAMap().get("objectA-2").getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithBuilder_nullGetter_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .putWithBuilder(null, "objectA-1", new Builder<>(new ObjectA()))
                    .as("objectA-1")
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .getAliasMap();
        //@formatter:on
    }

    @Test(expected = NullPointerException.class)
    public void putWithBuilder_toNullMap()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .set(ObjectA::setStringObjectAMap, null)
                .putWithBuilder(ObjectA::getStringObjectAMap, "objectA-1", new Builder<>(new ObjectA()))
                    .as("objectA-1")
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .getAliasMap();
        //@formatter:on
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(ObjectA::getObjectAObjectBMap, "key", new Builder<>(new ObjectB()))
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        assertEquals("objectB", objectA.getObjectAObjectBMap().get(key).getText());
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue_aliasKeyFunction()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .set(ObjectA::setText, "objectA")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(
                        ObjectA::getStringObjectAMap,
                        ObjectA.class,"key", ObjectA::getText,
                        new Builder<>(new ObjectA()))
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        assertEquals(1, objectA.getStringObjectAMap().get("objectA").getNumber());
    }

    @Test
    public void putWithBuilder_itemValueFunction()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setStringIntegerMap, new HashMap<>())
                .putWithBuilder(ObjectA::getStringIntegerMap, "objectA-1", new Builder<>(new ObjectA()), ObjectA::getNumber)
                    .as("objectA-1")
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .putWithBuilder(ObjectA::getStringIntegerMap, "objectA-2", new Builder<>(new ObjectA()), ObjectA::getNumber)
                    .as("objectA-2")
                    .set(ObjectA::setNumber, 2)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        assertEquals((Integer) 1, objectA.getStringIntegerMap().get("objectA-1"));
        assertEquals((Integer) 2, objectA.getStringIntegerMap().get("objectA-2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithBuilder_itemValueFunction_nullFunction_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .set(ObjectA::setStringIntegerMap, new HashMap<>())
                .putWithBuilder(ObjectA::getStringIntegerMap, "objectA-1", new Builder<>(new ObjectA()), null)
                    .as("objectA-1")
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue_itemValueFunction()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(
                        ObjectA::getObjectAObjectBMap,
                        "key",
                        new Builder<>(new ObjectB()),
                        me -> me)
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        assertEquals("objectB", objectA.getObjectAObjectBMap().get(key).getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKeyAndBuilderForValue_itemValueFunction_nullFunction_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(
                        ObjectA::getObjectAObjectBMap,
                        "key",
                        new Builder<>(new ObjectB()),
                        null)
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue_functions()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .set(ObjectA::setText, "objectA")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(
                        ObjectA::getStringObjectAMap,
                        ObjectA.class,"key", ObjectA::getText,
                        new Builder<>(new ObjectA()),
                        me -> me)
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on

        assertEquals(1, objectA.getStringObjectAMap().get("objectA").getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKeyAndBuilderForValue_functions_nullAliasKeyFunction_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .set(ObjectA::setText, "objectA")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(
                        ObjectA::getStringObjectAMap,
                        ObjectA.class,"key", null,
                        new Builder<>(new ObjectA()),
                        me -> me)
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKeyAndBuilderForValue_functions_nullItemValueFunction_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .set(ObjectA::setText, "objectA")
                    .toParent(ObjectA.class)
                .putWithAliasForKeyAndBuilderForValue(
                        ObjectA::getStringObjectAMap,
                        ObjectA.class,"key", ObjectA::getText,
                        new Builder<>(new ObjectA()),
                        null)
                    .set(ObjectA::setNumber, 1)
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }
}
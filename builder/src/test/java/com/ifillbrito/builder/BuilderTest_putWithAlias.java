package com.ifillbrito.builder;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_putWithAlias
{
    @Test
    public void putWithAlias()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "the value")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectAObjectBMap, "key", "value")
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        Map<ObjectA, ObjectB> map = objectA.getObjectAObjectBMap();
        ObjectB value = map.get(key);
        assertEquals("the value", value.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAlias_nullGetter_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "the value")
                    .toParent(ObjectA.class)
                .putWithAlias(null, "key", "value")
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForKey()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("object b");
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(ObjectA::getObjectAObjectBMap, "key", objectB)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        Map<ObjectA, ObjectB> map = objectA.getObjectAObjectBMap();
        ObjectB value = map.get(key);
        assertEquals("object b", value.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKey_nullMapGetter()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("object b");
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(null, "key", objectB)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForValue()
    {
        ObjectA objectAForKey = new ObjectA();
        objectAForKey.setText("key");
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "object b")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(ObjectA::getObjectAObjectBMap, objectAForKey, "value")
                .build();
        //@formatter:on

        Map<ObjectA, ObjectB> map = objectA.getObjectAObjectBMap();
        ObjectB value = map.get(objectAForKey);
        assertEquals("object b", value.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForValue_nullGetter_exception()
    {
        ObjectA objectAForKey = new ObjectA();
        objectAForKey.setText("key");
        //@formatter:off
        new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "object b")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(null, objectAForKey, "value")
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliases()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectAObjectBMap, ObjectA.class, "objectA", me -> me, "value")
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        ObjectB objectB = objectA.getObjectAObjectBMap().get(key);
        assertEquals("objectB", objectB.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliases_nullGetter_exception()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(null, ObjectA.class, "objectA", me -> me, "value")
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForKey_function()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("objectB");

        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(ObjectA::getObjectAObjectBMap, ObjectA.class, "objectA", me -> me, objectB)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        ObjectB expectedB = objectA.getObjectAObjectBMap().get(key);
        assertEquals("objectB", expectedB.getText());
    }

    @Test
    public void putWithAliases2()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectAObjectBMap, "objectA", ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        ObjectB objectB = objectA.getObjectAObjectBMap().get(key);
        assertEquals("objectB", objectB.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliases2_nullMapGetter_exception()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(null, "objectA", ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForValue2()
    {
        ObjectA key = new ObjectA();

        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(ObjectA::getObjectAObjectBMap, key, ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on

        ObjectB objectB = objectA.getObjectAObjectBMap().get(key);
        assertEquals("objectB", objectB.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForValue2_nullGetter_exception()
    {
        ObjectA key = new ObjectA();

        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(null, key, ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliases3()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectAObjectBMap, ObjectA.class, "objectA", me->me, ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        ObjectB objectB = objectA.getObjectAObjectBMap().get(key);
        assertEquals("objectB", objectB.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliases3_nullGetter_exception()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(null, ObjectA.class, "objectA", me->me, ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on
    }

    @Test(expected = NullPointerException.class)
    public void putWithAliases3_nullKeyFunction_exception()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectAObjectBMap, ObjectA.class, "objectA", null, ObjectB.class, "value" , me -> me)
                .build();
        //@formatter:on
    }

    @Test(expected = NullPointerException.class)
    public void putWithAliases3_nullValueFunction_exception()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, new Builder<>(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, new Builder<>(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectAObjectBMap, ObjectA.class, "objectA", me->me, ObjectB.class, "value" , null)
                .build();
        //@formatter:on
    }
}
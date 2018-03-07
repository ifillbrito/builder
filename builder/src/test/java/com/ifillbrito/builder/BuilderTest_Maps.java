package com.ifillbrito.builder;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_Maps
{
    @Test
    public void put()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setMap, new HashMap<>())
                .put(ObjectA::getMap, "key", 1)
                .build();

        assertEquals((Integer) 1, objectA.getMap().get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_nullGetter_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setMap, new HashMap<>())
                .put(null, "key", 1)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void put_toNullMap_exception()
    {
        // The map in ObjectA is not initialized.
        Builder.of(new ObjectA())
                .put(ObjectA::getMap, "key", 1)
                .build();
    }

    @Test
    public void putWithAlias()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "the value")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectsMap, "key", "value")
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        Map<ObjectA, ObjectB> map = objectA.getObjectsMap();
        ObjectB value = map.get(key);
        assertEquals("the value", value.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAlias_invalidKeyAlias_exception()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "the value")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectsMap, "key", "value")
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAlias_invalidValueAlias_exception()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectsMap, "key", "value")
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAlias_nullGetter()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
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
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(ObjectA::getObjectsMap, "key", objectB)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        Map<ObjectA, ObjectB> map = objectA.getObjectsMap();
        ObjectB value = map.get(key);
        assertEquals("object b", value.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKey_nullMapGetter()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("object b");
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(null, "key", objectB)
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKey_invalidKeyAlias()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("object b");
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(null, "KEY", objectB)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForValue()
    {
        ObjectA objectAForKey = new ObjectA();
        objectAForKey.setText("key");
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "object b")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(ObjectA::getObjectsMap, objectAForKey, "value")
                .build();
        //@formatter:on

        Map<ObjectA, ObjectB> map = objectA.getObjectsMap();
        ObjectB value = map.get(objectAForKey);
        assertEquals("object b", value.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForValue_nullGetter_exception()
    {
        ObjectA objectAForKey = new ObjectA();
        objectAForKey.setText("key");
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "object b")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(null, objectAForKey, "value")
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForValue_invalidValueAlias_exception()
    {
        ObjectA objectAForKey = new ObjectA();
        objectAForKey.setText("key");
        //@formatter:off
        Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "object b")
                    .toParent(ObjectA.class)
                .putWithAliasForValue(ObjectA::getObjectsMap, objectAForKey, "invalidKey")
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliases()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectsMap, ObjectA.class, "objectA", me -> me, "value")
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        ObjectB objectB = objectA.getObjectsMap().get(key);
        assertEquals("objectB", objectB.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliases_nullGetter_exception()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(null, ObjectA.class, "objectA", me -> me, "value")
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliases_invalidKeyAlias_exception()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectsMap, ObjectA.class, "invalidKey", me -> me, "value")
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliases_invalidValueAlias_exception()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .setWithBuilder(ObjectA::setObjectB, Builder.of(new ObjectB()))
                    .as("value")
                    .set(ObjectB::setText, "objectB")
                    .toParent(ObjectA.class)
                .putWithAlias(ObjectA::getObjectsMap, ObjectA.class, "objectA", me -> me, "invalidKey")
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForKey_function()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("objectB");

        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(ObjectA::getObjectsMap, ObjectA.class, "objectA", me -> me, objectB)
                .build();
        //@formatter:on

        ObjectA key = objectA.getObjectA();
        ObjectB expectedB = objectA.getObjectsMap().get(key);
        assertEquals("objectB", expectedB.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putWithAliasForKey_function_invalidKey()
    {
        ObjectB objectB = new ObjectB();
        objectB.setText("objectB");

        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .setWithBuilder(ObjectA::setObjectA, Builder.of(new ObjectA()))
                    .as("objectA")
                    .set(ObjectA::setText, "the key")
                    .toParent(ObjectA.class)
                .putWithAliasForKey(ObjectA::getObjectsMap, ObjectA.class, "invalidKey", me -> me, objectB)
                .build();
        //@formatter:on
    }

    @Test
    public void putWithAliasForValue1()
    {
    }

    @Test
    public void put2()
    {
    }

    @Test
    public void putWithAliases2()
    {
    }

    @Test
    public void putWithAliasForKey2()
    {
    }

    @Test
    public void putWithAliasForValue2()
    {
    }

    @Test
    public void put3()
    {
    }

    @Test
    public void putWithAliases3()
    {
    }

    @Test
    public void putWithAliasForKey3()
    {
    }

    @Test
    public void putWithAliasForValue3()
    {
    }

    @Test
    public void putAll()
    {
    }

    @Test
    public void putWithBuilder()
    {
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue()
    {
    }

    @Test
    public void putWithBuilder1()
    {
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue1()
    {
    }

    @Test
    public void putWithBuilder2()
    {
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue2()
    {
    }

    @Test
    public void put4()
    {
    }

    @Test
    public void putWithAliasForKeyAndBuilderForValue3()
    {
    }
}
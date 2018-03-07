package com.ifillbrito.builder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest
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

    @Test
    public void add()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .add(ObjectA::getList, "A")
                .add(ObjectA::getList, "B")
                .add(ObjectA::getList, "C")
                .add(ObjectA::getList, "D")
                .build();

        assertEquals(4, objectA.getList().size());
    }

    @Test(expected = NullPointerException.class)
    public void add_toNullList_exception()
    {
        Builder.of(new ObjectA())
                .add(ObjectA::getList, "A")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_nullGetter_exception()
    {
        Builder.of(new ObjectA())
                .add(null, "A")
                .build();
    }

    @Test
    public void addAll_getterAndList()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList, Arrays.asList("A", "B", "C"))
                .build();

        assertEquals(3, objectA.getList().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_nullGetter_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(null, Arrays.asList("A", "B", "C"))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_nullList_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList, null)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void addAll_listToNullList_exception()
    {
        Builder.of(new ObjectA())
                .addAll(ObjectA::getList, Arrays.asList("A", "B", "C"))
                .build();
    }

    @Test
    public void addWithAlias()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .addWithAlias(ObjectA::getObjectsA, "a")
                .build();

        assertEquals("object a", objectA.getObjectsA().get(0).getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithAlias_invalidAlias_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .addWithAlias(ObjectA::getObjectsA, "b")
                .build();
    }

    @Test
    public void addWithAlias_function()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "a", ObjectA::getText)
                .build();

        assertEquals("object a", objectA.getList().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithAlias_invalidAliasAndFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "b", ObjectA::getText)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithAlias_aliasAndNullFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "a", null)
                .build();
    }

    @Test
    public void addWithBuilder()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                    .set(ObjectA::setText, "child 2")
                    .toParent()
                .build();
        //@formatter:on

        assertEquals("child 1", objectA.getObjectsA().get(0).getText());
        assertEquals("child 2", objectA.getObjectsA().get(1).getText());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addWithBuilder_exception()
    {
        Builder.of(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                .set(ObjectA::setText, "child 1")
                .build(); // build() not allowed here. toParent() must be called.
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullGetter1_exception()
    {
        Builder.of(new ObjectA())
                .addWithBuilder(null, Builder.of(new ObjectA()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullBuilder_exception()
    {
        Builder.of(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, null);
    }

    @Test
    public void addWithBuilder_builderAndFunction()
    {
        //@formatter:off
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(ObjectA::getList, Builder.of(new ObjectA()), ObjectA::getText)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on

        assertEquals("child 1", objectA.getList().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullFunction_exception()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(ObjectA::getList, Builder.of(new ObjectA()), null)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullBuilderAndFunction_exception()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(ObjectA::getList, null, ObjectA::getText)
                    .set(ObjectA::setText, "child 1")
                    .toParent(ObjectA.class)
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullGetter2_exception()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(null, Builder.of(new ObjectA()), ObjectA::getText)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on
    }

    @Test(expected = NullPointerException.class)
    public void addWithBuilder_intoNullList_exception()
    {
        //@formatter:off
        Builder.of(new ObjectA())
                .addWithBuilder(ObjectA::getList, Builder.of(new ObjectA()), ObjectA::getText)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on
    }

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

    @Test
    public void toParent()
    {
    }

    @Test
    public void getAliasMap()
    {
    }
}
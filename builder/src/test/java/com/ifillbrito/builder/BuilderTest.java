package com.ifillbrito.builder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    public void setWithAlias_setterAndAlias()
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
    public void setWithAlias_nullSetterAndAlias()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(null, "alias")
                .build();
    }

    @Test
    public void setWithAlias_setterAndNullAlias_positive()
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
    public void setWithAlias_setterAndNonDefinedAlias_exception()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectA, "another alias")
                .build();
    }

    @Test(expected = ClassCastException.class)
    public void setWithAlias_setterAndAlias_classCastException()
    {
        Builder.of(new ObjectA())
                .as("alias")
                .set(ObjectA::setText, "text")
                .setWithAlias(ObjectA::setObjectB, "alias")
                .build();
    }

    @Test
    public void setWithAlias_setterAndAliasTypeAndAliasAndFunction()
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
    public void setWithAlias_nullSetterAndAliasTypeAndAliasAndFunction_exception()
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
    public void setWithAlias_setterAndAliasTypeAndAliasAndNullFunction_exception()
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

    @Test(expected = ClassCastException.class)
    public void setWithAlias_setterAndAliasTypeAndAliasAndFunction_classCastException()
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
    public void setWithBuilder_setterAndBuilder()
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
    public void setWithBuilder_nullSetterAndBuilder_exception()
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
    public void setWithBuilder_setterAndBuilderAndFunction()
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
    public void setWithBuilder_setterAndBuilderAndNullFunction_exception()
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
    public void add_getterAndValue()
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
    public void add_nullGetterAndValue_exception()
    {
        Builder.of(new ObjectA())
                .add(null, "A")
                .build();
    }

    @Test
    public void add_getterAndValueAndFunction()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .add(ObjectA::getList, "A", String::toLowerCase)
                .build();

        assertEquals("a", objectA.getList().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void add_getterAndValueAndFunctionToNullList_exception()
    {
        Builder.of(new ObjectA())
                .add(ObjectA::getList, "A", String::toLowerCase)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_nullGetterValueAndFunction_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .add(null, "A", String::toLowerCase)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_getterValueAndNullFunction_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .add(ObjectA::getList, "A", null)
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
    public void addAll_nullGetterAndList_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(null, Arrays.asList("A", "B", "C"))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_getterAndNullList_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList, null)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void addAll_getterAndListToNullList_exception()
    {
        Builder.of(new ObjectA())
                .addAll(ObjectA::getList, Arrays.asList("A", "B", "C"))
                .build();
    }

    @Test
    public void addAll_getterAndValueAndFunction()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList,
                        Arrays.asList(1, 2, 3),
                        list -> list
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList()))
                .build();

        assertEquals(3, objectA.getList().size());
        assertEquals("1", objectA.getList().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_nullGetterAndValueAndFunction_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(null,
                        Arrays.asList(1, 2, 3),
                        list -> list
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList()))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_getterAndValueAndNullFunction_exception()
    {
        Builder.of(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList, Arrays.asList(1, 2, 3), null)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void addAll_getterAndValueAndFunctionToNullList_exception()
    {
        Builder.of(new ObjectA())
                .addAll(ObjectA::getList,
                        Arrays.asList(1, 2, 3),
                        list -> list
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList()))
                .build();
    }

    @Test
    public void addWithAlias_getterAndAlias()
    {
        ObjectA objectA = Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .addWithAlias(ObjectA::getObjectsA, "a")
                .build();

        assertEquals("object a", objectA.getObjectsA().get(0).getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithAlias_getterAndNonDefinedAlias_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .addWithAlias(ObjectA::getObjectsA, "b")
                .build();
    }

    @Test
    public void addWithAlias_getterAndAliasAndFunction()
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
    public void addWithAlias_getterAndNonDefinedAliasAndFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "b", ObjectA::getText)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithAlias_getterAndAliasAndNullFunction_exception()
    {
        Builder.of(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "a", null)
                .build();
    }

    @Test
    public void addWithBuilder_getterAndBuilder()
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
    public void addWithBuilder_getterAndBuilder_exception()
    {
        Builder.of(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                .set(ObjectA::setText, "child 1")
                .build(); // build() not allowed here. toParent() must be called.
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullGetterAndBuilder_exception()
    {
        Builder.of(new ObjectA())
                .addWithBuilder(null, Builder.of(new ObjectA()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_getterAndNullBuilder_exception()
    {
        Builder.of(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, null);
    }

    @Test
    public void addWithBuilder_getterAndBuilderAndFunction()
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
    public void addWithBuilder_getterAndBuilderAndNullFunction_exception()
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
    public void addWithBuilder_getterAndNullBuilderAndFunction_exception()
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
    public void addWithBuilder_nullGetterAndBuilderAndFunction_exception()
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
    public void addWithBuilder_getterAndBuilderAndFunctionIntoNullList_exception()
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
    }

    @Test
    public void putWithAliases()
    {
    }

    @Test
    public void putWithAliasForKey()
    {
    }

    @Test
    public void putWithAliasForValue()
    {
    }

    @Test
    public void put1()
    {
    }

    @Test
    public void putWithAliases1()
    {
    }

    @Test
    public void putWithAliasForKey1()
    {
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
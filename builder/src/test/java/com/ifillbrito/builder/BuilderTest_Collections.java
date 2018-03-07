package com.ifillbrito.builder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class BuilderTest_Collections
{
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
}
package com.ifillbrito.builder;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class Builder_addWithBuilder_Test
{
    @Test
    public void addWithBuilder()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, new Builder<>(new ObjectA()))
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .addWithBuilder(ObjectA::getObjectsA, new Builder<>(new ObjectA()))
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
        new Builder<>(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, new Builder<>(new ObjectA()))
                .set(ObjectA::setText, "child 1")
                .build(); // build() not allowed here. toParent() must be called.
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullGetter1_exception()
    {
        new Builder<>(new ObjectA())
                .addWithBuilder(null, new Builder<>(new ObjectA()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullBuilder_exception()
    {
        new Builder<>(new ObjectA())
                .addWithBuilder(ObjectA::getObjectsA, null);
    }

    @Test
    public void addWithBuilder_builderAndFunction()
    {
        //@formatter:off
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(ObjectA::getList, new Builder<>(new ObjectA()), ObjectA::getText)
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
        new Builder<>(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(ObjectA::getList, new Builder<>(new ObjectA()), null)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithBuilder_nullBuilderAndFunction_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
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
        new Builder<>(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addWithBuilder(null, new Builder<>(new ObjectA()), ObjectA::getText)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on
    }

    @Test(expected = NullPointerException.class)
    public void addWithBuilder_intoNullList_exception()
    {
        //@formatter:off
        new Builder<>(new ObjectA())
                .addWithBuilder(ObjectA::getList, new Builder<>(new ObjectA()), ObjectA::getText)
                    .set(ObjectA::setText, "child 1")
                    .toParent()
                .build();
        //@formatter:on
    }
}
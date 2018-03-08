package com.ifillbrito.builder;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class Builder_addWithAlias_Test
{
    @Test
    public void addWithAlias()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .addWithAlias(ObjectA::getObjectsA, "a")
                .build();

        assertEquals("object a", objectA.getObjectsA().get(0).getText());
    }

    @Test
    public void addWithAlias_function()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "a", ObjectA::getText)
                .build();

        assertEquals("object a", objectA.getList().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithAlias_aliasAndNullFunction_exception()
    {
        new Builder<>(new ObjectA())
                .as("a")
                .set(ObjectA::setText, "object a")
                .set(ObjectA::setList, new ArrayList<>())
                .addWithAlias(ObjectA::getList, ObjectA.class, "a", null)
                .build();
    }
}
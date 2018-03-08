package com.github.ifillbrito.builder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 25.01.18.
 */
public class Builder_add_Test
{
    @Test
    public void add()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
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
        new Builder<>(new ObjectA())
                .add(ObjectA::getList, "A")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_nullGetter_exception()
    {
        new Builder<>(new ObjectA())
                .add(null, "A")
                .build();
    }

    @Test
    public void addAll_getterAndList()
    {
        ObjectA objectA = new Builder<>(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList, Arrays.asList("A", "B", "C"))
                .build();

        assertEquals(3, objectA.getList().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_nullGetter_exception()
    {
        new Builder<>(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(null, Arrays.asList("A", "B", "C"))
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAll_nullList_exception()
    {
        new Builder<>(new ObjectA())
                .set(ObjectA::setList, new ArrayList<>())
                .addAll(ObjectA::getList, null)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void addAll_listToNullList_exception()
    {
        new Builder<>(new ObjectA())
                .addAll(ObjectA::getList, Arrays.asList("A", "B", "C"))
                .build();
    }
}
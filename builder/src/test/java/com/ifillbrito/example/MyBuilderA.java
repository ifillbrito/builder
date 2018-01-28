package com.ifillbrito.example;

import com.ifillbrito.builder.BaseBuilder;
import com.ifillbrito.builder.ObjectA;
import com.ifillbrito.builder.ObjectB;

import java.util.ArrayList;

/**
 * Created by gjib on 28.01.18.
 */
public class MyBuilderA extends BaseBuilder<ObjectA, MyBuilderA>
{
    protected MyBuilderA(ObjectA object)
    {
        super(object);
    }

    public static MyBuilderA of (ObjectA objectA)
    {
        return new MyBuilderA(objectA);
    }

    public MyBuilderA withText (String text)
    {
        return super.set(ObjectA::setText, text);
    }

    public MyBuilderA withNumber (int number)
    {
        return super.set(ObjectA::setNumber, number);
    }

    public MyBuilderA addObjectA ()
    {
        if (object.getObjectsA() == null) object.setObjectsA(new ArrayList<>());
        return super.addWithBuilder(ObjectA::getObjectsA, MyBuilderA.of(new ObjectA()));
    }

    public MyBuilderB addObjectB ()
    {
        if (object.getObjectsB() == null) object.setObjectsB(new ArrayList<>());
        return super.addWithBuilder(ObjectA::getObjectsB, MyBuilderB.of(new ObjectB()));
    }

    public MyBuilderA toObjectA()
    {
        return super.toParent(ObjectA.class);
    }
}

package com.github.ifillbrito.example;

import com.github.ifillbrito.builder.BaseBuilder;
import com.github.ifillbrito.builder.ObjectA;
import com.github.ifillbrito.builder.ObjectB;

/**
 * Created by gjib on 28.01.18.
 */
public class MyBuilderB extends BaseBuilder<ObjectB, MyBuilderB>
{
    protected MyBuilderB(ObjectB object)
    {
        super(object);
    }

    public static MyBuilderB of(ObjectB objectB)
    {
        return new MyBuilderB(objectB);
    }

    public MyBuilderB withText(String text)
    {
        return super.set(ObjectB::setText, text);
    }

    public MyBuilderB withFlag(boolean flag)
    {
        return super.set(ObjectB::setFlag, flag);
    }

    public MyBuilderA toObjectA()
    {
        return super.toParent(ObjectA.class);
    }
}

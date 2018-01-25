package com.ifillbrito.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjib on 25.01.18.
 */
public class ObjectB
{
    private String text;
    private ObjectA objectA;
    private boolean flag;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public ObjectA getObjectA()
    {
        return objectA;
    }

    public void setObjectA(ObjectA objectA)
    {
        this.objectA = objectA;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }
}

package com.ifillbrito.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjib on 25.01.18.
 */
public class ObjectA
{
    private String text;
    private int number;
    private ObjectB objectB;
    private ObjectA objectA;
    private List<String> list;
    private List<ObjectA> objectList = new ArrayList<>();

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public ObjectB getObjectB()
    {
        return objectB;
    }

    public void setObjectB(ObjectB objectB)
    {
        this.objectB = objectB;
    }

    public ObjectA getObjectA()
    {
        return objectA;
    }

    public void setObjectA(ObjectA objectA)
    {
        this.objectA = objectA;
    }

    public List<String> getList()
    {
        return list;
    }

    public void setList(List<String> list)
    {
        this.list = list;
    }

    public List<ObjectA> getObjectList()
    {
        return objectList;
    }

    public void setObjectList(List<ObjectA> objectList)
    {
        this.objectList = objectList;
    }
}

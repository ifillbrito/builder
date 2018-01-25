package com.ifillbrito.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjib on 25.01.18.
 */
public class ObjectA
{
    private String text;
    private int number;
    private ObjectB objectB;
    private ObjectA objectA;
    private List<ObjectA> uninitializedList;
    private List<ObjectA> initializedList = new ArrayList<>();
    private Map<String, ObjectA> uninitializedMap;
    private Map<String, ObjectA> initializedMap = new HashMap<>();

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

    public List<ObjectA> getUninitializedList()
    {
        return uninitializedList;
    }

    public void setUninitializedList(List<ObjectA> uninitializedList)
    {
        this.uninitializedList = uninitializedList;
    }

    public List<ObjectA> getInitializedList()
    {
        return initializedList;
    }

    public void setInitializedList(List<ObjectA> initializedList)
    {
        this.initializedList = initializedList;
    }

    public Map<String, ObjectA> getUninitializedMap()
    {
        return uninitializedMap;
    }

    public void setUninitializedMap(Map<String, ObjectA> uninitializedMap)
    {
        this.uninitializedMap = uninitializedMap;
    }

    public Map<String, ObjectA> getInitializedMap()
    {
        return initializedMap;
    }

    public void setInitializedMap(Map<String, ObjectA> initializedMap)
    {
        this.initializedMap = initializedMap;
    }
}

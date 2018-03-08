package com.github.ifillbrito.builder;

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
    private List<String> list;
    private List<ObjectA> objectsA = new ArrayList<>();
    private List<ObjectB> objectsB = new ArrayList<>();
    private Map<String, Integer> stringIntegerMap;
    private Map<ObjectA, ObjectB> objectAObjectBMap = new HashMap<>();
    private Map<String, ObjectA> stringObjectAMap = new HashMap<>();

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

    public List<ObjectA> getObjectsA()
    {
        return objectsA;
    }

    public void setObjectsA(List<ObjectA> objectsA)
    {
        this.objectsA = objectsA;
    }

    public List<ObjectB> getObjectsB()
    {
        return objectsB;
    }

    public void setObjectsB(List<ObjectB> objectsB)
    {
        this.objectsB = objectsB;
    }

    public Map<String, Integer> getStringIntegerMap()
    {
        return stringIntegerMap;
    }

    public void setStringIntegerMap(Map<String, Integer> stringIntegerMap)
    {
        this.stringIntegerMap = stringIntegerMap;
    }

    public Map<ObjectA, ObjectB> getObjectAObjectBMap()
    {
        return objectAObjectBMap;
    }

    public Map<String, ObjectA> getStringObjectAMap()
    {
        return stringObjectAMap;
    }

    public void setStringObjectAMap(Map<String, ObjectA> stringObjectAMap)
    {
        this.stringObjectAMap = stringObjectAMap;
    }
}

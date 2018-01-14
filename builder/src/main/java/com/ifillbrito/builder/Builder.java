package com.ifillbrito.builder;

/**
 * Created by gjib on 13.01.18.
 */
public class Builder<Type> extends BaseBuilder<Type, Builder<Type>>
{
    protected Builder(Type object)
    {
        super(object);
    }

    public static <T> Builder<T> of(T object)
    {
        return new Builder<>(object);
    }
}

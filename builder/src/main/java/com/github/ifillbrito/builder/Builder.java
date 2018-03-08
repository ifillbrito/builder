package com.github.ifillbrito.builder;

/**
 * Created by gjib on 13.01.18.
 */
public class Builder<Type> extends BaseBuilder<Type, Builder<Type>>
{
    public Builder(Type object)
    {
        super(object);
    }
}

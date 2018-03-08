package com.ifillbrito.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by gjib on 12.01.18.
 */
public class BaseBuilder<Type, GenericBuilder extends FluentBuilder<Type, GenericBuilder>>
        implements FluentBuilder<Type, GenericBuilder>
{

    protected Type object;
    protected GenericBuilder parent;
    protected Object parentObject;
    protected BiConsumer parentConsumer;
    protected Function parentFunctionForConsumer;
    protected Collection parentCollection;
    protected Map parentMap;
    protected Object parentKey;
    protected Function parentFunctionForMapValue;
    protected Map<String, Object> aliasMap = new HashMap<>();

    public BaseBuilder(Type object)
    {
        this.object = object;
    }

    private ParentSetterType parentSetterType;

    private enum ParentSetterType
    {
        OBJECT,
        COLLECTION,
        MAP
    }


    // Setting Simple Fields
    @Override
    public <Value> GenericBuilder set(
            BiConsumer<Type, Value> consumer,
            Value value
    )
    {
        verifySetter(consumer);
        consumer.accept(object, value);
        return (GenericBuilder) this;
    }

    @Override
    public <Value> GenericBuilder setWithAlias(
            BiConsumer<Type, Value> consumer,
            String alias
    )
    {
        Value value = (Value) getByAlias(alias);
        return set(consumer, value);
    }

    @Override
    public <Alias, Value> GenericBuilder setWithAlias(
            BiConsumer<Type, Value> consumer,
            Class<Alias> aliasType,
            String alias,
            Function<Alias, Value> function
    )
    {
        Alias argument = (Alias) getByAlias(alias);
        verifyFunction(function);
        return set(consumer, function.apply(argument));
    }

    @Override
    public <NewType, Value, NewBuilder extends FluentBuilder<NewType, NewBuilder>> NewBuilder setWithBuilder(
            BiConsumer<Type, Value> consumer,
            NewBuilder builder
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) builder;
        newBuilder.parent = this;
        newBuilder.aliasMap = aliasMap;
        verifySetter(consumer);
        newBuilder.parentConsumer = consumer;
        newBuilder.parentSetterType = ParentSetterType.OBJECT;
        newBuilder.parentObject = object;
        return (NewBuilder) newBuilder;
    }

    @Override
    public <NewType, Value, NewBuilder extends FluentBuilder<NewType, NewBuilder>> NewBuilder setWithBuilder(
            BiConsumer<Type, Value> consumer,
            NewBuilder builder,
            Function<NewType, Value> function
    )
    {
        verifyFunction(function);
        BaseBuilder newBuilder = (BaseBuilder) setWithBuilder(consumer, builder);
        newBuilder.parentFunctionForConsumer = function;
        return (NewBuilder) newBuilder;
    }

    // Setting Collection Fields
    @Override
    public <Item, ListOrSet extends Collection<Item>> GenericBuilder add(
            Function<Type, ListOrSet> collectionGetter,
            Item value
    )
    {
        verifyGetter(collectionGetter);
        ListOrSet targetCollection = collectionGetter.apply(object);
        verifyTargetCollection(targetCollection);
        targetCollection.add(value);
        return (GenericBuilder) this;
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>> GenericBuilder addAll(
            Function<Type, ListOrSet> collectionGetter,
            ListOrSet collection
    )
    {
        verifyGetter(collectionGetter);
        verifyCollection(collection);
        ListOrSet targetCollection = collectionGetter.apply(object);
        verifyTargetCollection(targetCollection);
        targetCollection.addAll(collection);
        return (GenericBuilder) this;
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>> GenericBuilder addWithAlias(
            Function<Type, ListOrSet> collectionGetter,
            String alias
    )
    {
        Item item = (Item) getByAlias(alias);
        return add(collectionGetter, item);
    }

    @Override
    public <Alias, Item, ListOrSet extends Collection<Item>> GenericBuilder addWithAlias(
            Function<Type, ListOrSet> collectionGetter,
            Class<Alias> aliasType,
            String alias,
            Function<Alias, Item> function
    )
    {
        verifyFunction(function);
        Alias targetObject = (Alias) getByAlias(alias);
        return add(collectionGetter, function.apply(targetObject));
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder addWithBuilder(
            Function<Type, ListOrSet> collectionGetter,
            NewBuilder builder
    )
    {
        verifyGetter(collectionGetter);
        verifyBuilder(builder);
        BaseBuilder newBuilder = (BaseBuilder) builder;
        newBuilder.parent = this;
        newBuilder.aliasMap = aliasMap;
        newBuilder.parentCollection = collectionGetter.apply(object);
        newBuilder.parentSetterType = ParentSetterType.COLLECTION;
        return (NewBuilder) newBuilder;
    }

    @Override
    public <Item, BuilderType, ListOrSet extends Collection<Item>, NewBuilder extends FluentBuilder<BuilderType, NewBuilder>> NewBuilder addWithBuilder(
            Function<Type, ListOrSet> collectionGetter,
            NewBuilder builder,
            Function<BuilderType, Item> function
    )
    {
        verifyFunction(function);
        verifyGetter(collectionGetter);
        verifyBuilder(builder);
        BaseBuilder newBuilder = (BaseBuilder) builder;
        newBuilder.parent = this;
        ListOrSet targetCollection = collectionGetter.apply(object);
        verifyTargetCollection(targetCollection);
        newBuilder.parentCollection = targetCollection;
        newBuilder.aliasMap = aliasMap;
        newBuilder.parentFunctionForConsumer = function;
        newBuilder.parentSetterType = ParentSetterType.COLLECTION;
        return (NewBuilder) newBuilder;
    }

    // Setting Map Fields
    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder put(
            Function<Type, MapType> mapGetter,
            Key key,
            Value value
    )
    {
        verifyGetter(mapGetter);
        MapType targetMap = mapGetter.apply(object);
        verifyTargetMap(targetMap);
        targetMap.put(key, value);
        return (GenericBuilder) this;
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            String valueAlias
    )
    {
        Key key = (Key) getByAlias(keyAlias);
        Value value = (Value) getByAlias(valueAlias);
        return put(mapGetter, key, value);
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            Value value
    )
    {
        Key key = (Key) getByAlias(keyAlias);
        return put(mapGetter, key, value);
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            Key key,
            String valueAlias
    )
    {
        Value value = (Value) getByAlias(valueAlias);
        return put(mapGetter, key, value);
    }

    @Override
    public <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> aliasKeyFunction,
            String valueAlias
    )
    {
        Alias key = (Alias) getByAlias(keyAlias);
        Value value = (Value) getByAlias(valueAlias);
        return put(mapGetter, aliasKeyFunction.apply(key), value);
    }

    @Override
    public <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> aliasKeyFunction,
            Value value
    )
    {
        Alias key = (Alias) getByAlias(keyAlias);
        return put(mapGetter, aliasKeyFunction.apply(key), value);
    }

    @Override
    public <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            Class<Alias> valueAliasType,
            String valueAlias,
            Function<Alias, Value> aliasValueFunction
    )
    {
        Key key = (Key) getByAlias(keyAlias);
        Alias value = (Alias) getByAlias(valueAlias);
        return put(mapGetter, key, aliasValueFunction.apply(value));
    }

    @Override
    public <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            Key key,
            Class<Alias> valueAliasType,
            String valueAlias,
            Function<Alias, Value> valueFunction
    )
    {
        Alias value = (Alias) getByAlias(valueAlias);
        return put(mapGetter, key, valueFunction.apply(value));
    }

    @Override
    public <KeyAlias, ValueAlias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            Class<KeyAlias> keyAliasType,
            String keyAlias,
            Function<KeyAlias, Key> keyAliasKeyFunction,
            Class<ValueAlias> valueAliasType,
            String valueAlias,
            Function<ValueAlias, Value> valueAliasValueFunction
    )
    {
        KeyAlias key = (KeyAlias) getByAlias(keyAlias);
        ValueAlias value = (ValueAlias) getByAlias(valueAlias);
        return put(mapGetter, keyAliasKeyFunction.apply(key), valueAliasValueFunction.apply(value));
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putAll(
            Function<Type, MapType> mapGetter,
            MapType map
    )
    {
        verifyGetter(mapGetter);
        MapType targetMap = mapGetter.apply(object);
        verifyTargetMap(targetMap);
        targetMap.putAll(map);
        return (GenericBuilder) this;
    }

    @Override
    public <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithBuilder(
            Function<Type, MapType> mapGetter,
            Key key,
            NewBuilder builder
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) builder;
        newBuilder.parent = this;
        newBuilder.aliasMap = aliasMap;
        verifyGetter(mapGetter);
        MapType targetMap = mapGetter.apply(object);
        verifyTargetMap(targetMap);
        newBuilder.parentMap = targetMap;
        newBuilder.parentSetterType = ParentSetterType.MAP;
        newBuilder.parentKey = key;
        return (NewBuilder) newBuilder;
    }

    @Override
    public <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            NewBuilder builder
    )
    {
        Key key = (Key) getByAlias(keyAlias);
        return putWithBuilder(mapGetter, key, builder);
    }

    @Override
    public <Item, Alias, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> aliasKeyFunction,
            NewBuilder builder
    )
    {
        Alias key = (Alias) getByAlias(keyAlias);
        return putWithBuilder(mapGetter, aliasKeyFunction.apply(key), builder);
    }

    @Override
    public <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithBuilder(
            Function<Type, MapType> mapGetter,
            Key key,
            NewBuilder valueBuilder,
            Function<Item, Value> itemValueFunction
    )
    {
        verifyFunction(itemValueFunction);
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        return (NewBuilder) putWithBuilder(mapGetter, key, newBuilder);
    }

    @Override
    public <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            NewBuilder valueBuilder,
            Function<Item, Value> itemValueFunction
    )
    {
        verifyFunction(itemValueFunction);
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        Key key = (Key) getByAlias(keyAlias);
        return (NewBuilder) putWithBuilder(mapGetter, key, newBuilder);
    }

    @Override
    public <Item, Alias, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> aliasKeyFunction,
            NewBuilder valueBuilder,
            Function<Item, Value> itemValueFunction
    )
    {
        verifyFunction(itemValueFunction);
        verifyFunction(aliasKeyFunction);
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        Alias key = (Alias) getByAlias(keyAlias);
        return (NewBuilder) putWithBuilder(mapGetter, aliasKeyFunction.apply(key), newBuilder);
    }

    @Override
    public GenericBuilder as(String alias)
    {
        aliasMap.put(alias, object);
        return (GenericBuilder) this;
    }

    public GenericBuilder toParent()
    {
        return toParent((Class<Type>) object.getClass());
    }

    @Override
    public <T, ParentBuilder extends FluentBuilder<T, ParentBuilder>> ParentBuilder toParent(Class<T> type)
    {
        if ( parent == null ) throw new UnsupportedOperationException("No parent has been defined for this builder");
        switch ( parentSetterType )
        {
            case OBJECT:
                if ( parentFunctionForConsumer == null ) parentConsumer.accept(parentObject, object);
                else parentConsumer.accept(parentObject, parentFunctionForConsumer.apply(object));
                break;

            case COLLECTION:
                if ( parentFunctionForConsumer == null ) parentCollection.add(object);
                else parentCollection.add(parentFunctionForConsumer.apply(object));
                break;

            case MAP:
                if ( parentFunctionForMapValue == null ) parentMap.put(parentKey, object);
                else parentMap.put(parentKey, parentFunctionForMapValue.apply(object));
        }
        return (ParentBuilder) parent;
    }

    @Override
    public Map<String, Object> getAliasMap()
    {
        return aliasMap;
    }

    @Override
    public Type build()
    {
        if ( parentSetterType != null )
            throw new UnsupportedOperationException(
                    "This builder was created from another builder. " +
                            "Call the build() method of the root builder.");
        return object;
    }

    // protected methods

    protected Object getByAlias(String keyAlias)
    {
        Object key = aliasMap.get(keyAlias);
        verifyAlias(keyAlias, key);
        return key;
    }

    protected <T> void verifyAlias(String aliasName, T alias)
    {
        if ( alias == null )
            throw new IllegalArgumentException("The alias " + aliasName + " has not been defined. The alias must be defined before it is used.");
    }

    protected void verifySetter(Object setter)
    {
        verifyArgument(setter, "setter");
    }

    protected void verifyGetter(Object getter)
    {
        verifyArgument(getter, "getter");
    }

    protected void verifyFunction(Object function)
    {
        verifyArgument(function, "function");
    }

    protected void verifyCollection(Object collection)
    {
        verifyArgument(collection, "collection");
    }

    protected void verifyBuilder(Object builder)
    {
        verifyArgument(builder, "builder");
    }

    protected void verifyArgument(Object object, String name)
    {
        if ( object == null ) throw new IllegalArgumentException("The " + name + " cannot be null.");
    }

    protected void verifyTargetCollection(Object collection)
    {
        verifyNotNull(collection, "collection");
    }

    protected void verifyTargetMap(Object map)
    {
        verifyNotNull(map, "map");
    }

    protected void verifyNotNull(Object object, String name)
    {
        if ( object == null ) throw new NullPointerException("The target " + name + " cannot be null.");
    }
}

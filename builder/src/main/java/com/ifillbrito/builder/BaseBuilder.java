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
    protected Function parentFunctionForMapKey;
    protected Function parentFunctionForMapValue;
    protected Map<String, Object> aliasMap = new HashMap<>();

    protected BaseBuilder(Type object)
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
        if ( consumer == null ) throw new IllegalArgumentException("The setter cannot be null.");
        consumer.accept(object, value);
        return (GenericBuilder) this;
    }

    @Override
    public <Argument, Value> GenericBuilder set(
            BiConsumer<Type, Value> consumer,
            Argument argument,
            Function<Argument, Value> function
    )
    {
        if ( function == null ) throw new IllegalArgumentException("The function cannot be null.");
        return set(consumer, function.apply(argument));
    }

    @Override
    public <Value> GenericBuilder setWithAlias(
            BiConsumer<Type, Value> consumer,
            String alias
    )
    {
        Value value = (Value) aliasMap.get(alias);
        if ( value == null )
            throw new IllegalArgumentException("The alias " + alias + " has not been defined. The alias must be defined before it is used.");
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
        Alias argument = (Alias) aliasMap.get(alias);
        return set(consumer, argument, function);
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
        if ( function == null ) throw new IllegalArgumentException("The function cannot be null.");
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
        ListOrSet targetCollection = collectionGetter.apply(object);
        if (targetCollection == null) throw new NullPointerException("The target collection is null.");
        targetCollection.add(value);
        return (GenericBuilder) this;
    }

    @Override
    public <Argument, Item, ListOrSet extends Collection<Item>> GenericBuilder add(
            Function<Type, ListOrSet> collectionGetter,
            Argument argument,
            Function<Argument, Item> function
    )
    {
        ListOrSet targetCollection = collectionGetter.apply(object);
        targetCollection.add(function.apply(argument));
        return (GenericBuilder) this;
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>> GenericBuilder addAll(
            Function<Type, ListOrSet> collectionGetter,
            ListOrSet collection
    )
    {
        ListOrSet aCollection = collectionGetter.apply(object);
        aCollection.addAll(collection);
        return (GenericBuilder) this;
    }

    @Override
    public <Argument, Item, ListOrSet extends Collection<Item>> GenericBuilder addAll(
            Function<Type, ListOrSet> collectionGetter,
            Argument argument,
            Function<Argument, ListOrSet> function
    )
    {
        ListOrSet targetCollection = collectionGetter.apply(object);
        targetCollection.addAll(function.apply(argument));
        return (GenericBuilder) this;
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>> GenericBuilder addWithAlias(
            Function<Type, ListOrSet> collectionGetter,
            String alias
    )
    {
        return add(collectionGetter, (Item) aliasMap.get(alias));
    }

    @Override
    public <Alias, Item, ListOrSet extends Collection<Item>> GenericBuilder addWithAlias(
            Function<Type, ListOrSet> collectionGetter,
            Class<Alias> aliasType,
            String alias,
            Function<Alias, Item> function
    )
    {
        return add(collectionGetter, function.apply((Alias) aliasMap.get(alias)));
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder addWithBuilder(
            Function<Type, ListOrSet> collectionGetter,
            NewBuilder builder
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) builder;
        newBuilder.parent = this;
        newBuilder.aliasMap = aliasMap;
        newBuilder.parentCollection = collectionGetter.apply(object);
        newBuilder.parentSetterType = ParentSetterType.COLLECTION;
        return (NewBuilder) newBuilder;
    }

    @Override
    public <Item, ListOrSet extends Collection<Item>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder addWithBuilder(
            Function<Type, ListOrSet> collectionGetter,
            NewBuilder builder,
            Function<Type, Item> function
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) addWithBuilder(collectionGetter, builder);
        newBuilder.parentFunctionForConsumer = function;
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
        MapType map = mapGetter.apply(object);
        map.put(key, value);
        return (GenericBuilder) this;
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            String valueAlias
    )
    {
        return put(mapGetter, (Key) aliasMap.get(keyAlias), (Value) aliasMap.get(valueAlias));
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            Value value
    )
    {
        return put(mapGetter, (Key) aliasMap.get(keyAlias), value);
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            Key key,
            String valueAlias
    )
    {
        return put(mapGetter, key, (Value) aliasMap.get(valueAlias));
    }

    @Override
    public <Argument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder put(
            Function<Type, MapType> mapGetter,
            Argument keyArgument,
            Function<Argument, Key> keyFunction,
            Value value
    )
    {
        return put(mapGetter, keyFunction.apply(keyArgument), value);
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
        return put(mapGetter, aliasKeyFunction.apply((Alias) aliasMap.get(keyAlias)), (Value) aliasMap.get(valueAlias));
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
        return put(mapGetter, aliasKeyFunction.apply((Alias) aliasMap.get(keyAlias)), value);
    }

    @Override
    public <Argument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            Argument keyArgument,
            Function<Argument, Key> keyFunction,
            String valueAlias
    )
    {
        return put(mapGetter, keyFunction.apply(keyArgument), (Value) aliasMap.get(valueAlias));
    }

    @Override
    public <Argument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder put(
            Function<Type, MapType> mapGetter,
            Key key, Argument valueArgument,
            Function<Argument, Value> argumentValueFunction
    )
    {
        return put(mapGetter, key, argumentValueFunction.apply(valueArgument));
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
        return put(mapGetter, (Key) aliasMap.get(keyAlias), aliasValueFunction.apply((Alias) aliasMap.get(valueAlias)));
    }

    @Override
    public <Argument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            Argument valueArgument,
            Function<Argument, Value> valueFunction
    )
    {
        return put(mapGetter, (Key) aliasMap.get(keyAlias), valueFunction.apply(valueArgument));
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
        return put(mapGetter, key, valueFunction.apply((Alias) aliasMap.get(valueAlias)));
    }

    @Override
    public <KeyArgument, ValueArgument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder put(
            Function<Type, MapType> mapGetter,
            KeyArgument keyArgument,
            Function<KeyArgument, Key> keyArgumentKeyFunction,
            ValueArgument valueArgument,
            Function<ValueArgument, Value> valueArgumentValueFunction
    )
    {
        return put(mapGetter, keyArgumentKeyFunction.apply(keyArgument), valueArgumentValueFunction.apply(valueArgument));
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
        return put(mapGetter, keyAliasKeyFunction.apply((KeyAlias) aliasMap.get(keyAlias)), valueAliasValueFunction.apply((ValueAlias) aliasMap.get(valueAlias)));
    }

    @Override
    public <Alias, ValueArgument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> aliasKeyFunction,
            ValueArgument valueArgument,
            Function<ValueArgument, Value> valueArgumentValueFunction
    )
    {
        return put(mapGetter, aliasKeyFunction.apply((Alias) aliasMap.get(keyAlias)), valueArgumentValueFunction.apply(valueArgument));
    }

    @Override
    public <Alias, KeyArgument, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            KeyArgument keyArgument,
            Function<KeyArgument, Key> keyArgumentKeyFunction,
            Class<Alias> valueAliasType,
            String valueAlias,
            Function<Alias, Value> aliasValueFunction
    )
    {
        return put(mapGetter, keyArgumentKeyFunction.apply(keyArgument), aliasValueFunction.apply((Alias) aliasMap.get(valueAlias)));
    }

    @Override
    public <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putAll(
            Function<Type, MapType> mapGetter,
            MapType map
    )
    {
        MapType targetMap = mapGetter.apply(object);
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
        newBuilder.parentMap = mapGetter.apply(object);
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
        return putWithBuilder(mapGetter, (Key) aliasMap.get(keyAlias), builder);
    }

    @Override
    public <Item, Argument, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithBuilder(
            Function<Type, MapType> mapGetter,
            Argument keyArgument,
            Function<Argument, Key> keyFunction,
            NewBuilder builder
    )
    {
        return putWithBuilder(mapGetter, keyFunction.apply(keyArgument), builder);
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
        return putWithBuilder(mapGetter, aliasKeyFunction.apply((Alias) aliasMap.get(keyAlias)), builder);
    }

    @Override
    public <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithBuilder(
            Function<Type, MapType> mapGetter,
            Key key,
            NewBuilder valueBuilder,
            Function<Item, Value> itemValueFunction
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        return (NewBuilder) putWithBuilder(mapGetter, key, newBuilder);
    }

    @Override
    public <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            NewBuilder valueBuilder,
            Function<Item, Key> itemValueFunction
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        return (NewBuilder) putWithBuilder(mapGetter, (Key) aliasMap.get(keyAlias), newBuilder);
    }

    @Override
    public <Item, KeyArgument, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder put(
            Function<Type, MapType> mapGetter,
            KeyArgument keyArgument,
            Function<KeyArgument, Key> keyArgumentKeyFunction,
            NewBuilder valueBuilder,
            Function<Item, Value> itemValueFunction
    )
    {
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        return (NewBuilder) putWithBuilder(mapGetter, keyArgumentKeyFunction.apply(keyArgument), newBuilder);
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
        BaseBuilder newBuilder = (BaseBuilder) valueBuilder;
        newBuilder.parentFunctionForMapValue = itemValueFunction;
        return (NewBuilder) putWithBuilder(mapGetter, aliasKeyFunction.apply((Alias) aliasMap.get(keyAlias)), newBuilder);
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
                if ( parentConsumer == null )
                    throw new IllegalArgumentException("The setter of the parent builder cannot be null.");
                if ( parentFunctionForConsumer == null ) parentConsumer.accept(parentObject, object);
                else parentConsumer.accept(parentObject, parentFunctionForConsumer.apply(object));
                return (ParentBuilder) parent;

            case COLLECTION:
                if ( parentCollection == null )
                    throw new IllegalArgumentException("The collection getter of the parent builder cannot be null.");
                if ( parentFunctionForConsumer == null ) parentCollection.add(object);
                else parentCollection.add(parentFunctionForConsumer.apply(object));
                return (ParentBuilder) parent;
            case MAP:
                if ( parentMap == null )
                    throw new IllegalArgumentException("The map getter of the parent builder cannot be null.");
                if ( parentFunctionForMapKey == null && parentFunctionForMapValue == null )
                    parentMap.put(parentKey, object);
                else if ( parentFunctionForMapKey != null && parentFunctionForMapValue == null )
                    parentMap.put(parentFunctionForMapKey.apply(parentKey), object);
                else if ( parentFunctionForMapKey == null )
                    parentMap.put(parentKey, parentFunctionForMapValue.apply(object));
                else parentMap.put(parentFunctionForMapKey.apply(parentKey), parentFunctionForMapValue.apply(object));
                return (ParentBuilder) parent;
        }
        throw new RuntimeException("[Internal Error] No parentSetterType was defined.");
    }

    @Override
    public Map<String, Object> getAliasMap()
    {
        return aliasMap;
    }

    @Override
    public Type build()
    {
        return object;
    }
}

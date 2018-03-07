package com.ifillbrito.builder;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by gjib on 11.01.18.
 */
public interface FluentBuilder<Type, GenericBuilder extends FluentBuilder<Type, GenericBuilder>>
{
    // objects
    <Value> GenericBuilder set(
            BiConsumer<Type, Value> consumer,
            Value value
    );

    <Value> GenericBuilder setWithAlias(
            BiConsumer<Type, Value> consumer,
            String alias
    );

    <Alias, Value> GenericBuilder setWithAlias(
            BiConsumer<Type, Value> consumer,
            Class<Alias> aliasType,
            String alias,
            Function<Alias, Value> function
    );

    <NewType, Value, NewBuilder extends FluentBuilder<NewType, NewBuilder>> NewBuilder setWithBuilder(
            BiConsumer<Type, Value> consumer,
            NewBuilder builder
    );

    <NewType, Value, NewBuilder extends FluentBuilder<NewType, NewBuilder>> NewBuilder setWithBuilder(
            BiConsumer<Type, Value> consumer,
            NewBuilder builder,
            Function<NewType, Value> function
    );

    // collections
    <Item, ListOrSet extends Collection<Item>> GenericBuilder add(
            Function<Type, ListOrSet> collectionGetter,
            Item value
    );

    <Item, ListOrSet extends Collection<Item>> GenericBuilder addAll(
            Function<Type, ListOrSet> collectionGetter,
            ListOrSet collection
    );

    <Item, ListOrSet extends Collection<Item>> GenericBuilder addWithAlias(
            Function<Type, ListOrSet> collectionGetter,
            String alias
    );

    <Alias, Item, ListOrSet extends Collection<Item>> GenericBuilder addWithAlias(
            Function<Type, ListOrSet> collectionGetter,
            Class<Alias> aliasType,
            String alias,
            Function<Alias, Item> function
    );

    <Item, ListOrSet extends Collection<Item>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder addWithBuilder(
            Function<Type, ListOrSet> collectionGetter,
            NewBuilder builder
    );

    <Item, BuilderType, ListOrSet extends Collection<Item>, NewBuilder extends FluentBuilder<BuilderType, NewBuilder>> NewBuilder addWithBuilder(
            Function<Type, ListOrSet> collectionGetter,
            NewBuilder builder,
            Function<BuilderType, Item> function
    );

    // maps
    <Key, Value, MapType extends Map<Key, Value>> GenericBuilder put(
            Function<Type, MapType> mapGetter,
            Key key,
            Value value
    );

    <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            String valueAlias
    );

    <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            Value value
    );

    <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            Key key,
            String valueAlias
    );

    <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> keyFunction,
            String valueAlias
    );

    <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForKey(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> keyFunction,
            Value value
    );

    <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            Class<Alias> valueAliasType,
            String valueAlias,
            Function<Alias, Value> valueFunction
    );

    <Alias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAliasForValue(
            Function<Type, MapType> mapGetter,
            Key key,
            Class<Alias> valueAliasType,
            String valueAlias,
            Function<Alias, Value> valueFunction
    );

    <KeyAlias, ValueAlias, Key, Value, MapType extends Map<Key, Value>> GenericBuilder putWithAlias(
            Function<Type, MapType> mapGetter,
            Class<KeyAlias> keyAliasType,
            String keyAlias,
            Function<KeyAlias, Key> keyFunction,
            Class<ValueAlias> valueAliasType,
            String valueAlias,
            Function<ValueAlias, Value> valueFunction
    );

    <Key, Value, MapType extends Map<Key, Value>> GenericBuilder putAll(
            Function<Type, MapType> mapGetter,
            MapType map
    );
    
    // map with builders
    <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithBuilder(
            Function<Type, MapType> mapGetter,
            Key key,
            NewBuilder builder
    );

    <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            NewBuilder builder
    );

    <Item, Alias, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> keyFunction,
            NewBuilder builder
    );

    <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithBuilder(
            Function<Type, MapType> mapGetter,
            Key key,
            NewBuilder valueBuilder,
            Function<Item, Value> valueFunction
    );

    <Item, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            String keyAlias,
            NewBuilder valueBuilder,
            Function<Item, Key> valueFunction
    );

    <Item, Alias, Key, Value, MapType extends Map<Key, Value>, NewBuilder extends FluentBuilder<Item, NewBuilder>> NewBuilder putWithAliasForKeyAndBuilderForValue(
            Function<Type, MapType> mapGetter,
            Class<Alias> keyAliasType,
            String keyAlias,
            Function<Alias, Key> keyFunction,
            NewBuilder valueBuilder,
            Function<Item, Value> valueFunction
    );

    // general
    GenericBuilder as(String alias);

    <T, ParentBuilder extends FluentBuilder<T, ParentBuilder>> ParentBuilder toParent(Class<T> type);

    Map<String, Object> getAliasMap();

    Type build();
}

package com.ifillbrito.buildertest;

import com.ifillbrito.builder.Builder;
import com.ifillbrito.example.Address;
import com.ifillbrito.example.ContactInformation;
import com.ifillbrito.example.Person;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by gjib on 09.01.18.
 */
public class BuilderTest
{
    @Test
    public void set_setterAndValue_positive()
    {
        // when
        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob")
                .set(Person::setLastName, "Smith")
                .build();

        // then
        assertEquals("Bob", bob.getName());
        assertEquals("Smith", bob.getLastName());
    }

    @Test
    public void set_setterAndValueAndFunction_positive()
    {
        // when
        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob", String::toUpperCase)
                .set(Person::setLastName, "Smith", String::toUpperCase)
                .build();

        // then
        assertEquals("BOB", bob.getName());
        assertEquals("SMITH", bob.getLastName());
    }

    @Test
    public void set_setterAndAlias_positive()
    {
        // when
        Person bob = Builder.of(new Person())
                .as("bob")
                .set(Person::setName, "Bob")
                .set(Person::setLastName, "Smith")
                .setUsingAlias(Person::setPartner, "bob")
                .build();

        // then
        assertEquals("Bob", bob.getPartner().getName());
        assertEquals("Smith", bob.getPartner().getLastName());
    }

    @Test
    public void set_setterAndAliasAndFunction_positive()
    {
        // when
        Person bob = Builder.of(new Person())
                .as("bob")
                .set(Person::setName, "Bob")
                .set(Person::setLastName, "Smith")
                .setUsingAlias(Person::setPartner, Person.class, "bob", p -> {
                    Person ana = new Person();
                    ana.setName("Anna");
                    ana.setLastName(p.getLastName());
                    return ana;
                })
                .build();

        // then
        assertEquals("Anna", bob.getPartner().getName());
        assertEquals("Smith", bob.getPartner().getLastName());
    }

    @Test
    public void set_setterAndBuilder_positive()
    {
        // when

        // there is another test that shows how to put the following data directly in the builder
        Map<String, String> telephoneNumbers = new HashMap<>();
        telephoneNumbers.put("home", "11-111-1111");
        telephoneNumbers.put("work", "22-222-2222");

        //@formatter:off
        Person bob = Builder.of(new Person())
                .as("bob")
                .set(Person::setName, "Bob")
                .set(Person::setLastName, "Smith")
                .setUsingBuilder(Person::setPartner, Builder.of(new Person()))
                    .set(Person::setName, "Anna")
                    .set(Person::setLastName, "Smith")
                    .setUsingBuilder(Person::setContactInformation, Builder.of(new ContactInformation()))
                        .set(ContactInformation::setTelephoneNumbers, telephoneNumbers)
                        .toParent(Person.class)
                    .toParent(Person.class)
                .build();
        //@formatter:on

        // then
        assertEquals("Anna", bob.getPartner().getName());
        assertEquals("Smith", bob.getPartner().getLastName());
        assertEquals("11-111-1111", bob.getPartner().getContactInformation().getTelephoneNumbers().get("home"));
        assertEquals("22-222-2222", bob.getPartner().getContactInformation().getTelephoneNumbers().get("work"));
    }

    @Test
    public void set_setterAndBuilderAndFunction_positive()
    {
        // when
        //@formatter:off
        Person bob = Builder.of(new Person())
                .as("bob")
                .set(Person::setName, "Bob")
                .setUsingBuilder(Person::setLastName, Builder.of(new Person()), Person::getLastName)
                    .set(Person::setLastName, "Smith")
                    .toParent(Person.class)
                .build();
        //@formatter:on

        // then
        assertEquals("Bob", bob.getName());
        assertEquals("Smith", bob.getLastName());
    }

    @Test
    public void add_GetterAndValue_positive()
    {
        // when
        // there is another test that shows how to put the following data directly in the builder
        Person ana = new Person();
        ana.setName("Anna");
        Person mary = new Person();
        mary.setName("Mary");

        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob")
                .set(Person::setChildren, new ArrayList<>())
                .add(Person::getChildren, ana)
                .add(Person::getChildren, mary)
                .build();

        // then
        assertEquals(2, bob.getChildren().size());
        assertEquals("Anna", bob.getChildren().get(0).getName());
        assertEquals("Mary", bob.getChildren().get(1).getName());
    }

    @Test
    public void add_GetterAndValueAndFunction_positive()
    {
        // when
        Function<String, Person> createPerson = name -> {
            Person result = new Person();
            result.setName(name);
            return result;
        };

        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob")
                .set(Person::setChildren, new ArrayList<>())
                .add(Person::getChildren, "Anna", createPerson)
                .add(Person::getChildren, "Mary", createPerson)
                .build();

        // then
        assertEquals(2, bob.getChildren().size());
        assertEquals("Anna", bob.getChildren().get(0).getName());
        assertEquals("Mary", bob.getChildren().get(1).getName());
    }

    @Test
    public void addAll_GetterAndValue_positive()
    {
        // when
        // there is another test that shows how to put the following data directly in the builder
        Person ana = new Person();
        ana.setName("Anna");
        Person mary = new Person();
        mary.setName("Mary");

        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob")
                .set(Person::setChildren, new ArrayList<>())
                .addAll(Person::getChildren, Arrays.asList(ana, mary))
                .build();

        // then
        assertEquals(2, bob.getChildren().size());
        assertEquals("Anna", bob.getChildren().get(0).getName());
        assertEquals("Mary", bob.getChildren().get(1).getName());
    }

    @Test
    public void addAll_GetterAndValueAndFunction_positive()
    {
        // when
        Function<List<String>, List<Person>> createPersons = names -> names.stream()
                .map(name -> {
                    Person person = new Person();
                    person.setName(name);
                    return person;
                })
                .collect(Collectors.toList());

        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob")
                .set(Person::setChildren, new ArrayList<>())
                .addAll(Person::getChildren, Arrays.asList("Anna", "Mary"), createPersons)
                .build();

        // then
        assertEquals(2, bob.getChildren().size());
        assertEquals("Anna", bob.getChildren().get(0).getName());
        assertEquals("Mary", bob.getChildren().get(1).getName());
    }

    @Test
    public void addUsingBuilder_GetterAndBuilder_positive()
    {
        // when
        //@formatter:off
        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob").as("bob")
                .set(Person::setChildren, new ArrayList<>())
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Anna")
                    .toParent(Person.class)
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Mary")
                    .toParent(Person.class)
                .build();
        //@formatter:on

        // then
        assertEquals(2, bob.getChildren().size());
        assertEquals("Anna", bob.getChildren().get(0).getName());
        assertEquals("Mary", bob.getChildren().get(1).getName());
    }

    @Test
    public void addUsingBuilder_GetterAndBuilderAndFunction_positive()
    {
        // when
        Function<Person, Person> upperCaseName = p -> {
            p.setName(p.getName().toUpperCase());
            return p;
        };

        //@formatter:off
        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob").as("bob")
                .set(Person::setChildren, new ArrayList<>())
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()), upperCaseName)
                    .set(Person::setName, "Anna")
                    .toParent(Person.class)
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()), upperCaseName)
                    .set(Person::setName, "Mary")
                    .toParent(Person.class)
                .build();
        //@formatter:on

        // then
        assertEquals(2, bob.getChildren().size());
        assertEquals("ANNA", bob.getChildren().get(0).getName());
        assertEquals("MARY", bob.getChildren().get(1).getName());
    }

    @Test
    public void addUsingAlias_GetterAndAlias_positive()
    {
        // when
        //@formatter:off
        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob").as("bob")
                .set(Person::setChildren, new ArrayList<>())
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Anna").as("anna")
                    .toParent(Person.class)
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Mary").as("mary")
                    .toParent(Person.class)
                .setUsingBuilder(Person::setPartner, Builder.of(new Person()))
                    .set(Person::setName, "Laura")
                    .set(Person::setChildren, new ArrayList<>())
                    .addUsingAlias(Person::getChildren, "anna")
                    .addUsingAlias(Person::getChildren, "mary")
                    .toParent(Person.class)
                .build();
        //@formatter:on

        // then
        assertEquals(2, bob.getPartner().getChildren().size());
        assertEquals("Anna", bob.getPartner().getChildren().get(0).getName());
        assertEquals("Mary", bob.getPartner().getChildren().get(1).getName());
    }

    @Test
    public void addUsingAlias_GetterAndAliasAndFunction_positive()
    {
        // when
        Function<Person, Person> upperCaseName = p -> {
            p.setName(p.getName().toUpperCase());
            return p;
        };

        //@formatter:off
        Person bob = Builder.of(new Person())
                .set(Person::setName, "Bob").as("bob")
                .set(Person::setChildren, new ArrayList<>())
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Anna").as("anna")
                    .toParent(Person.class)
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Mary").as("mary")
                    .toParent(Person.class)
                .setUsingBuilder(Person::setPartner, Builder.of(new Person()))
                    .set(Person::setName, "Laura")
                    .set(Person::setChildren, new ArrayList<>())
                    .addUsingAlias(Person::getChildren, Person.class, "anna", upperCaseName)
                    .addUsingAlias(Person::getChildren, Person.class, "mary", upperCaseName)
                    .toParent(Person.class)
                .build();
        //@formatter:on

        // then
        assertEquals(2, bob.getPartner().getChildren().size());
        assertEquals("ANNA", bob.getPartner().getChildren().get(0).getName());
        assertEquals("MARY", bob.getPartner().getChildren().get(1).getName());
    }

    @Test
    public void put_KeyValue_positive()
    {
        // when
        ContactInformation info = Builder.of(new ContactInformation())
                .set(ContactInformation::setTelephoneNumbers, new HashMap<>())
                .put(ContactInformation::getTelephoneNumbers, "home", "11-111-1111")
                .put(ContactInformation::getTelephoneNumbers, "work", "22-222-2222")
                .build();

        // then
        assertEquals("11-111-1111", info.getTelephoneNumbers().get("home"));
        assertEquals("22-222-2222", info.getTelephoneNumbers().get("work"));
    }

    @Test
    public void put_KeyBuilder_positive()
    {
        // when
        //@formatter:off
        ContactInformation info = Builder.of(new ContactInformation())
                .set(ContactInformation::setAddresses, new HashMap<>())
                .putUsingBuilder(ContactInformation::getAddresses, "home", Builder.of(new Address()))
                    .set(Address::setCity, "Frankfurt")
                    .set(Address::setCountry, "Germany")
                    .toParent(ContactInformation.class)
                .build();
        //@formatter:on

        // then
        assertEquals("Frankfurt", info.getAddresses().get("home").getCity());
        assertEquals("Germany", info.getAddresses().get("home").getCountry());
    }

    @Test
    public void completeExample()
    {
        // when
        //@formatter:off

        // create builder for Person
        Map<String,Object> aliasMap = Builder.of(new Person())
                .set(Person::setName, "Bob").as("bob")
                .set(Person::setLastName, "Smith")
                // add object ContactInformation to Person
                .setUsingBuilder(Person::setContactInformation, Builder.of(new ContactInformation()))
                    .as("bob-contact-info")
                    .set(ContactInformation::setTelephoneNumbers, new HashMap<>())
                    .put(ContactInformation::getTelephoneNumbers, "home", "11-111-1111")
                    // setup the map for addresses
                    .set(ContactInformation::setAddresses, new HashMap<>())
                    // put Address in the correspondng ContactInformation Map
                    .putUsingBuilder(ContactInformation::getAddresses, "home", Builder.of(new Address()))
                        .set(Address::setCity, "Frankfurt")
                        .set(Address::setCountry, "Germany")
                        .as("bob-home-address")
                        .toParent(ContactInformation.class)
                    .toParent(Person.class)
                // setup the list for children
                .set(Person::setChildren, new ArrayList<>())
                // add a child (Person) to Person
                .addUsingBuilder(Person::getChildren, Builder.of(new Person()))
                    .set(Person::setName, "Anna").as("anna")
                    // use Bob's last name
                    .setUsingAlias(Person::setLastName, Person.class, "bob", Person::getLastName)
                    // add object ContactInformation to Person
                    .setUsingBuilder(Person::setContactInformation, Builder.of(new ContactInformation()))
                        .set(ContactInformation::setAddresses, new HashMap<>())
                        // use Bob's address
                        .putUsingAliasForValue(ContactInformation::getAddresses, "home", "bob-home-address")
                        .toParent(Person.class)
                    .toParent(Person.class)
                .getAliasMap();
        //@formatter:on

        // then
        Person bob = (Person) aliasMap.get("bob");
        assertEquals("Bob", bob.getName());
        assertEquals("Smith", bob.getLastName());

        Person anna = (Person) aliasMap.get("anna");
        assertEquals("Smith", anna.getLastName());
        assertEquals(anna, bob.getChildren().get(0));
        assertEquals(aliasMap.get("bob-contact-info"), bob.getContactInformation());
        assertEquals(anna.getContactInformation().getAddresses().get("home"), aliasMap.get("bob-home-address"));


    }

}
# Fluent Generic Builder [Prototype]

This project provides a generic fluent builder that can be used for any object. The builder uses method reference to setup the values in the target object, so that reflection is not necessary.

## Example:
```java
Person person = Builder.of(new Person())
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
    .build();
``` 

For further examples take a look at the <a href='https://github.com/ifillbrito/fluent-builder/blob/master/builder/src/test/java/com/ifillbrito/buildertest/BuilderTest.java'>unit tests</a>.

## License

Copyright 2018 by Grebiel Ifill

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

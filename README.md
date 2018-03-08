[![travis-ci](https://travis-ci.org/ifillbrito/builder.svg?branch=master)](https://travis-ci.org/ifillbrito/builder)
[![codecov](https://codecov.io/gh/ifillbrito/builder/branch/master/graph/badge.svg)](https://codecov.io/gh/ifillbrito/builder)

This project provides a generic builder that can be used as it is for any object. However, it is recommended to extend the ``BaseBuilder`` class to assign more appropiate names to the builder methods and improve the code readability. The builder uses method reference to setup the values in the target object.

## Features
- Internal references (aliases)
- Support for adding elements to collections and maps (nested builders)
- Functions can be applied to internal builders

For more details about how to use the generic builder, take a look at the <a href='https://github.com/ifillbrito/fluent-builder/tree/master/builder/src/test/java/com/ifillbrito/builder'>unit tests</a>. The unit tests use the generic builder directly. Don't forget to extend the class ``BaseBuilder`` in order to improve code readability.

## Example:
In the following example we show how to build a basic object using three approaches:
1. Without using a builder
2. Using the generic builder
3. Using specific builders (extends ``BaseBuilder``)

The object to be built looks as follows:
````
main object     [text=main,         number=0]
    child       [text=child A1,     number=1]
        child   [text=child B11,    flag=true]
        child   [text=child B12,    flag=false]
    child       [text=child A2,     number=2]
        child   [text=child B21,    flag=true]
    child       [text=child A3,     number=3]
````

### Without using Builders
```java
ObjectA main = new ObjectA();
main.setText("main");
main.setNumber(0);

List<ObjectA> objectsA = new ArrayList<>();
main.setObjectsA(objectsA);
ObjectA childA1 = new ObjectA();
objectsA.add(childA1);
childA1.setText("child A1");
childA1.setNumber(1);

List<ObjectB> objectsB1 = new ArrayList<>();
childA1.setObjectsB(objectsB1);
ObjectB childB11 = new ObjectB();
objectsB1.add(childB11);
childB11.setText("child B11");
childB11.setFlag(true);

ObjectB childB12 = new ObjectB();
objectsB1.add(childB12);
childB12.setText("child B12");
childB12.setFlag(false);

ObjectA childA2 = new ObjectA();
objectsA.add(childA2);
childA2.setText("child A2");
childA2.setNumber(2);

List<ObjectB> objectsB2 = new ArrayList<>();
childA2.setObjectsB(objectsB2);
ObjectB childB21 = new ObjectB();
objectsB2.add(childB21);
childB21.setText("child B21");
childB21.setFlag(true);

ObjectA childA3 = new ObjectA();
objectsA.add(childA3);
childA3.setText("child A3");
childA3.setNumber(3);
``` 

### Using the Generic Builder
````java
ObjectA main = new Builder<>(new ObjectA())
  .set(ObjectA::setText, "main")
  .set(ObjectA::setNumber, 0)
  .set(ObjectA::setObjectsA, new ArrayList<>())
  .addWithBuilder(ObjectA::getObjectsA, new Builder<>(new ObjectA()))
      .set(ObjectA::setText, "child A1")
      .set(ObjectA::setNumber, 1)
      .set(ObjectA::setObjectsB, new ArrayList<>())
      .addWithBuilder(ObjectA::getObjectsB, new Builder<>(new ObjectB()))
          .set(ObjectB::setText, "child B11")
          .set(ObjectB::setFlag, true)
          .toParent(ObjectA.class)
      .addWithBuilder(ObjectA::getObjectsB, new Builder<>(new ObjectB()))
          .set(ObjectB::setText, "child B12")
          .set(ObjectB::setFlag, false)
          .toParent(ObjectA.class)
      .toParent(ObjectA.class)
  .addWithBuilder(ObjectA::getObjectsA, new Builder<>(new ObjectA()))
      .set(ObjectA::setText, "child A2")
      .set(ObjectA::setNumber, 2)
      .addWithBuilder(ObjectA::getObjectsB, new Builder<>(new ObjectB()))
          .set(ObjectB::setText, "child B21")
          .set(ObjectB::setFlag, true)
          .toParent(ObjectA.class)
      .toParent(ObjectA.class)
  .addWithBuilder(ObjectA::getObjectsA, new Builder<>(new ObjectA()))
      .set(ObjectA::setText, "child A3")
      .set(ObjectA::setNumber, 3)
      .toParent()
  .build();
````

### Using Specific Builders (``extends BaseBuilder``)
````java
ObjectA main = MyBuilderA.of(new ObjectA())
    .withText("main")
    .withNumber(0)
    .addObjectA()
        .withText("child A1")
        .withNumber(1)
        .addObjectB()
            .withText("child B11")
            .withFlag(true)
            .toObjectA()
        .addObjectB()
            .withText("child B12")
            .withFlag(false)
            .toObjectA()
        .toObjectA()
    .addObjectA()
        .withText("child A2")
        .withNumber(2)
        .addObjectB()
            .withText("child B21")
            .withFlag(true)
            .toObjectA()
        .toObjectA()
    .addObjectA()
        .withText("child A3")
        .withNumber(3)
        .toObjectA()
    .build();
````

For further details about ``MyBuilderA`` and ``MyBuilderB`` take a look at these <a href='https://github.com/ifillbrito/fluent-builder/tree/master/builder/src/test/java/com/ifillbrito/example'>files</a>.

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

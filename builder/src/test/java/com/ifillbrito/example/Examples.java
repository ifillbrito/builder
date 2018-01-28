package com.ifillbrito.example;

import com.ifillbrito.builder.Builder;
import com.ifillbrito.builder.ObjectA;
import com.ifillbrito.builder.ObjectB;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gjib on 28.01.18.
 */
public class Examples
{
    /*

        main object [text=main, number=0]
            child   [text=child A1, number=1]
                child   [text=child B11, flag=true]
                child   [text=child B12, flag=false]
            child   [text=child A2, number=2]
                child   [text=child B21, flag=true]
            child   [text=child A3, number=3]
     */

    @Test
    public void defineObjectWithoutUsingBuilder()
    {
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

        assertValues(main);
    }

    @Test
    public void defineObjectUsingGenericBuilder()
    {
        //@formatter:off
        ObjectA main = Builder.of(new ObjectA())
                .set(ObjectA::setText, "main")
                .set(ObjectA::setNumber, 0)
                .set(ObjectA::setObjectsA, new ArrayList<>())
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                    .set(ObjectA::setText, "child A1")
                    .set(ObjectA::setNumber, 1)
                    .set(ObjectA::setObjectsB, new ArrayList<>())
                    .addWithBuilder(ObjectA::getObjectsB, Builder.of(new ObjectB()))
                        .set(ObjectB::setText, "child B11")
                        .set(ObjectB::setFlag, true)
                        .toParent(ObjectA.class)
                    .addWithBuilder(ObjectA::getObjectsB, Builder.of(new ObjectB()))
                        .set(ObjectB::setText, "child B12")
                        .set(ObjectB::setFlag, false)
                        .toParent(ObjectA.class)
                    .toParent(ObjectA.class)
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                    .set(ObjectA::setText, "child A2")
                    .set(ObjectA::setNumber, 2)
                    .addWithBuilder(ObjectA::getObjectsB, Builder.of(new ObjectB()))
                        .set(ObjectB::setText, "child B21")
                        .set(ObjectB::setFlag, true)
                        .toParent(ObjectA.class)
                    .toParent(ObjectA.class)
                .addWithBuilder(ObjectA::getObjectsA, Builder.of(new ObjectA()))
                    .set(ObjectA::setText, "child A3")
                    .set(ObjectA::setNumber, 3)
                    .toParent()
                .build();
        //@formatter:on

        assertValues(main);
    }

    @Test
    public void defineObjectUsingSpecificBuilder()
    {
        //@formatter:off
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
        //@formatter:on

        assertValues(main);
    }


    private void assertValues(ObjectA main)
    {
        assertEquals("main", main.getText());
        assertEquals(0, main.getNumber());

        ObjectA childA1 = main.getObjectsA().get(0);
        assertEquals("child A1", childA1.getText());
        assertEquals(1, childA1.getNumber());

        ObjectB childB11 = childA1.getObjectsB().get(0);
        assertEquals("child B11", childB11.getText());
        assertTrue(childB11.isFlag());

        ObjectB childB12 = childA1.getObjectsB().get(1);
        assertEquals("child B12", childB12.getText());
        assertFalse(childB12.isFlag());

        ObjectA childA2 = main.getObjectsA().get(1);
        assertEquals("child A2", childA2.getText());
        assertEquals(2, childA2.getNumber());

        ObjectB childB21 = childA2.getObjectsB().get(0);
        assertEquals("child B21", childB21.getText());
        assertTrue(childB21.isFlag());

        ObjectA childA3 = main.getObjectsA().get(2);
        assertEquals("child A3", childA3.getText());
        assertEquals(3, childA3.getNumber());
    }
}

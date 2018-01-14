package com.ifillbrito.example;

import java.util.Date;
import java.util.List;

/**
 * Created by gjib on 09.01.18.
 */
public class Person
{
    private String name;
    private String lastName;
    private Date birthdate;
    private ContactInformation contactInformation;
    private Person Partner;
    private List<Person> children;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Date getBirthdate()
    {
        return birthdate;
    }

    public void setBirthdate(Date birthdate)
    {
        this.birthdate = birthdate;
    }

    public Person getPartner()
    {
        return Partner;
    }

    public void setPartner(Person partner)
    {
        Partner = partner;
    }

    public List<Person> getChildren()
    {
        return children;
    }

    public void setChildren(List<Person> children)
    {
        this.children = children;
    }

    public ContactInformation getContactInformation()
    {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation)
    {
        this.contactInformation = contactInformation;
    }
}

package com.ifillbrito.example;

import java.util.Map;

/**
 * Created by gjib on 12.01.18.
 */
public class ContactInformation
{
    private Map<String, String> telephoneNumbers;
    private Map<String, Address> addresses;

    public Map<String, String> getTelephoneNumbers()
    {
        return telephoneNumbers;
    }

    public void setTelephoneNumbers(Map<String, String> telephoneNumbers)
    {
        this.telephoneNumbers = telephoneNumbers;
    }

    public Map<String, Address> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(Map<String, Address> addresses)
    {
        this.addresses = addresses;
    }
}

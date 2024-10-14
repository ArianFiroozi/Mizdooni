package test;

import mizdooni.model.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class test { //TODO: Delete later
    @Test
    public void sampleTest1(){
        assertTrue(true);
    }

    @Test
    public void sampleTest2(){
        Address address= new Address("Iran", "Tehran", "UT");
        assertEquals("Iran", address.getCountry());

    }
}

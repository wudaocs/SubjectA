package com.td.ca.lib_test;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ReflectTestTest {

    @Test
    public void set() {
    }

    @Test
    public void get() throws ClassNotFoundException {

        Class c = Class.forName("com.td.ca.lib_test.ReflectTest");
        try {
            Object fa= c.newInstance();
            Method set = c.getMethod("set");
            Method get = c.getMethod("get");
            try {
                System.out.println("set" + set.invoke(fa));
                System.out.println("get" + get.invoke(fa));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        } catch (InstantiationException| IllegalAccessException |NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
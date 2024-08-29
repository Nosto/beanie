/*
 *  Copyright (c) 2024 Nosto Solutions Ltd All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Nosto Solutions Ltd ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into with
 *  Nosto Solutions Ltd.
 */
package com.nosto.beanie;

import org.junit.Ignore;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Ignore
public class TypeVariableJacksonBeanTest extends AbstractJacksonBeanTest<TypeVariableJacksonBeanTest.Concrete, TypeVariableJacksonBeanTest.Concrete> {

    public TypeVariableJacksonBeanTest() {
        super(TypeVariableJacksonBeanTest.Concrete.class);
    }

    @Override
    protected BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public abstract static class Base<T> extends AbstractTestBean {
        private final T t;

        public Base(T t) {
            this.t = t;
        }

        @SuppressWarnings("unused")
        public T getT() {
            return t;
        }
    }

    public static class Concrete extends Base<String> {

        @JsonCreator
        public Concrete(@JsonProperty("t") String t) {
            super(t);
        }
    }
}

/*
 *  Copyright (c) 2021 Nosto Solutions Ltd All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Nosto Solutions Ltd ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into with
 *  Nosto Solutions Ltd.
 */
package com.nosto.beanie;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author ollik1
 */
public class PolymorphicJacksonBeanTest extends AbstractJacksonBeanTest<PolymorphicJacksonBeanTest.Base, PolymorphicJacksonBeanTest.Concrete1> {

    public PolymorphicJacksonBeanTest() {
        super(Base.class, Concrete1.class);
    }

    @Test
    public void polymorphicDeserialization() throws JsonProcessingException {
        Base value = getMapper().readValue("{\"type\":\"t1\",\"x\":\"foo\"}", Base.class);
        assertEquals(Concrete1.class, value.getClass());
        assertEquals("foo", value.getX());
    }

    @Override
    protected BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider(mapper -> mapper.registerSubtypes(Concrete1.class));
    }

    @JsonIgnoreProperties("type")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
    public abstract static class Base extends AbstractTestBean {
        private final String x;

        public Base(String x) {
            this.x = x;
        }

        public String getX() {
            return x;
        }
    }

    @JsonTypeName("t1")
    public static class Concrete1 extends Base {
        @JsonCreator
        public Concrete1(@JsonProperty("x") String x) {
            super(x);
        }
    }
}

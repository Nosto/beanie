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

import org.jeasy.random.api.RandomizerRegistry;
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
public interface BeanieTest<T> {

    default RandomizerRegistry getRandomizerRegistry() {
        return new CustomRandomizerRegistry();
    }

    default BeanDescription getDescription(Class<?> klass) {
        JavaType javaType = getMapper().getTypeFactory()
                .constructType(klass);
        return getMapper()
                .getSerializationConfig()
                .introspect(javaType);
    }

    default ObjectMapper getMapper() {
        return getBeanieProvider().getMapper();
    }

    BeanieProvider getBeanieProvider();

    void assertEquals(Object expected, Object actual);

    void assertTrue(boolean condition, String message);

    void assertEquals(Object expected, Object actual, String message);
}

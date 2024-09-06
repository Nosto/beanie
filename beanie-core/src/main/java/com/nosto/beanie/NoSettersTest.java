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

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.util.List;
import java.util.stream.Collectors;

public interface NoSettersTest<T> extends BeanieTest<T> {

    /**
     * Check that there is no setters
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    default void testNoSetters(Class<? extends T> concreteClass) {
        List<BeanPropertyDefinition> properties = getDescription(concreteClass).findProperties()
                .stream()
                .filter(BeanPropertyDefinition::hasSetter)
                .toList();
        assertTrue(properties.isEmpty(), String.format("Properties should be immutable but the following properties have setters: %s",
                properties
                        .stream()
                        .map(BeanPropertyDefinition::getName)
                        .collect(Collectors.joining(","))));
    }
}

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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public interface NamingStrategyTest<T> extends BeanieTest<T> {

    /**
     * Test that all properties of a bean are named with a consistent naming strategy and all property names
     * comply with the bean's naming strategy.
     */
    default void testNamingStrategy(Class<? extends T> concreteClass) {
        BeanDescription beanDescription = getDescription(concreteClass);

        @SuppressWarnings("unchecked")
        Class<PropertyNamingStrategy> configuredNamingStrategy = Optional.ofNullable(getMapper().getPropertyNamingStrategy())
                // Required to get around compilation error
                .map(s -> (Class<PropertyNamingStrategy>) s.getClass())
                .orElse(PropertyNamingStrategy.class);

        @SuppressWarnings("unchecked")
        Class<PropertyNamingStrategy> beanPropertyNamingStrategy = Optional.ofNullable(beanDescription.getClassAnnotations().get(JsonNaming.class))
                .map(JsonNaming::value)
                // Required to get around compilation error
                .map(s -> (Class<PropertyNamingStrategy>) s)
                .orElse(configuredNamingStrategy);

        Map<Class<PropertyNamingStrategy>, List<String>> cases = beanDescription.findProperties()
                .stream()
                .map(BeanPropertyDefinition::getName)
                .collect(Collectors.groupingBy(name -> {
                    if (name.contains("_") && name.toLowerCase().equals(name)) {
                        // Required to get around compilation error
                        //noinspection unchecked
                        return (Class<PropertyNamingStrategy>) PropertyNamingStrategy.SNAKE_CASE.getClass();
                    } else if (name.toLowerCase().equals(name)) {
                        // Could be snake case or camel case, so let's assume the class's naming strategy.
                        return beanPropertyNamingStrategy;
                    } else {
                        // Required to get around compilation error
                        //noinspection unchecked
                        return (Class<PropertyNamingStrategy>) PropertyNamingStrategy.LOWER_CAMEL_CASE.getClass();
                    }
                }));
        assertEquals(1, cases.size(), cases.toString());

        Class<PropertyNamingStrategy> propertyNamingStrategy = cases.keySet()
                .stream()
                .findAny()
                .orElseThrow();

        assertEquals(beanPropertyNamingStrategy, propertyNamingStrategy);
    }
}
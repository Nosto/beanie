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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.KebabCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerDotCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public interface NamingStrategyTest<T> extends BeanieTest<T> {

    /**
     * Test that all properties of a bean are named with a consistent naming strategy and all property names
     * comply with the bean's naming strategy.
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    default void testNamingStrategy(Class<? extends T> concreteClass) {
        BeanDescription beanDescription = getDescription(concreteClass);

        @SuppressWarnings("unchecked")
        Class<PropertyNamingStrategy> configuredNamingStrategy = Optional.ofNullable(getMapper().getPropertyNamingStrategy())
                .map(Object::getClass)
                .map(NamingStrategyTest::getNamingStrategy)
                // Required to get around compilation error
                .map(klass -> (Class<PropertyNamingStrategy>) klass)
                .orElse(PropertyNamingStrategy.class);

        @SuppressWarnings("unchecked")
        Class<PropertyNamingStrategy> beanPropertyNamingStrategy = Optional.ofNullable(beanDescription.getClassAnnotations().get(JsonNaming.class))
                .map(JsonNaming::value)
                .map(NamingStrategyTest::getNamingStrategy)
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
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        assertEquals(beanPropertyNamingStrategy, propertyNamingStrategy);
    }

    private static Class<?> getNamingStrategy(Class<?> namingStrategy) {
        if (namingStrategy.equals(LowerCamelCaseStrategy.class)) {
            return PropertyNamingStrategy.LOWER_CAMEL_CASE.getClass();
        }
        if (namingStrategy.equals(UpperCamelCaseStrategy.class)) {
            return PropertyNamingStrategy.UPPER_CAMEL_CASE.getClass();
        }
        if (namingStrategy.equals(SnakeCaseStrategy.class)) {
            return PropertyNamingStrategy.SNAKE_CASE.getClass();
        }
        if (namingStrategy.equals(LowerCaseStrategy.class)) {
            return PropertyNamingStrategy.LOWER_CASE.getClass();
        }
        if (namingStrategy.equals(KebabCaseStrategy.class)) {
            return PropertyNamingStrategy.KEBAB_CASE.getClass();
        }
        if (namingStrategy.equals(LowerDotCaseStrategy.class)) {
            return PropertyNamingStrategy.LOWER_DOT_CASE.getClass();
        }
        return namingStrategy;
    }
}

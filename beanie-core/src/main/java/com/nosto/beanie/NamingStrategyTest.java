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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public interface NamingStrategyTest<T> extends BeanieTest<T> {

    /**
     * Test that all properties of a bean are named with a consistent naming strategy and all property names
     * comply with the bean's naming strategy.
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    default void testNamingStrategy(Class<? extends T> concreteClass) {
        BeanDescription beanDescription = getDescription(concreteClass);

        @SuppressWarnings({"unchecked", "rawtypes"})
        Class<? extends PropertyNamingStrategy> configuredNamingStrategy = Optional.ofNullable(getMapper().getPropertyNamingStrategy())
                .map(Object::getClass)
                .map(clazz -> (Class<? extends PropertyNamingStrategy>) clazz)
                .map(NamingStrategyTest::getNamingStrategy)
                .orElse((Class) PropertyNamingStrategies.LowerCamelCaseStrategy.class);

        @SuppressWarnings({"unchecked", "rawtypes"})
        Class<? extends PropertyNamingStrategy> beanPropertyNamingStrategy = Optional.ofNullable(beanDescription.getClassAnnotations().get(JsonNaming.class))
                .map(JsonNaming::value)
                .map(NamingStrategyTest::getNamingStrategy)
                .orElse((Class)configuredNamingStrategy);

        Map<Class<? extends PropertyNamingStrategy>, List<String>> cases = beanDescription.findProperties()
                .stream()
                .map(BeanPropertyDefinition::getName)
                .collect(Collectors.groupingBy(name -> {
                    if (name.contains("_") && name.toLowerCase().equals(name)) {
                        return PropertyNamingStrategies.SNAKE_CASE.getClass();
                    } else if (name.toLowerCase().equals(name)) {
                        // Could be snake case or camel case, so let's assume the class's naming strategy.
                        return beanPropertyNamingStrategy;
                    } else {
                        return PropertyNamingStrategies.LOWER_CAMEL_CASE.getClass();
                    }
                }));
        assertEquals(1, cases.size(), cases.toString());

        Class<? extends PropertyNamingStrategy> propertyNamingStrategy = cases.keySet()
                .stream()
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        assertEquals(beanPropertyNamingStrategy, propertyNamingStrategy);
    }

    private static Class<? extends PropertyNamingStrategy> getNamingStrategy(Class<? extends PropertyNamingStrategy> namingStrategy) {
        if (namingStrategy.equals(PropertyNamingStrategy.class)) {
            return PropertyNamingStrategies.LOWER_CAMEL_CASE.getClass();
        }
        if (namingStrategy.equals(PropertyNamingStrategy.UpperCamelCaseStrategy.class)) {
            return PropertyNamingStrategies.UPPER_CAMEL_CASE.getClass();
        }
        if (namingStrategy.equals(PropertyNamingStrategy.SnakeCaseStrategy.class)) {
            return PropertyNamingStrategies.SNAKE_CASE.getClass();
        }
        if (namingStrategy.equals(PropertyNamingStrategy.LowerCaseStrategy.class)) {
            return PropertyNamingStrategies.LOWER_CASE.getClass();
        }
        if (namingStrategy.equals(PropertyNamingStrategy.KebabCaseStrategy.class)) {
            return PropertyNamingStrategies.KEBAB_CASE.getClass();
        }
        if (namingStrategy.equals(PropertyNamingStrategy.LowerDotCaseStrategy.class)) {
            return PropertyNamingStrategies.LOWER_DOT_CASE.getClass();
        }
        return namingStrategy;
    }
}

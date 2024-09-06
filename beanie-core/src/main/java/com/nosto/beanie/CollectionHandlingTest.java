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
import com.nosto.beanie.jeasy.ForceAllNonPrimitivesAsNullRandomizerRegistry;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public interface CollectionHandlingTest<T> extends BeanieTest<T> {

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    default void testSerdeCollection(Class<? extends T> concreteClass) {
        EasyRandomParameters randomParameters = getEasyRandomParameters();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        randomParameters.randomizerRegistry(getRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T actualObj = getBeanieProvider().fromJSON(json, findRootType(concreteClass));
                        T expectedObj = getBeanieProvider().fromJSON(json, findRootType(concreteClass));
                        assertEquals(expectedObj, actualObj, json);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    default void testSerdeCollectionAsWell(Class<? extends T> concreteClass) {
        EasyRandomParameters randomParameters = getEasyRandomParameters();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        randomParameters.randomizerRegistry(getRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T actualObj = getBeanieProvider().fromJSON(json, concreteClass);
                        List<String> properties = getDescription(concreteClass).findProperties()
                                .stream()
                                .filter(property -> property.getPrimaryType().isCollectionLikeType())
                                .filter(property -> property.getAccessor().getValue(actualObj) == null)
                                .filter(property -> !property.getAccessor().hasAnnotation(Nullable.class))
                                .map(BeanPropertyDefinition::getName)
                                .toList();
                        assertTrue(properties.isEmpty(), "Following properties return null for collection types " + properties);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    private Class<? extends T> findRootType(Class<? extends T> clazz) {
        return clazz;
    }

    EasyRandomParameters getEasyRandomParameters();
}

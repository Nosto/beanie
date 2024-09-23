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

import java.io.IOException;
import java.util.stream.IntStream;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import com.nosto.beanie.jeasy.ExcludedMapAndCollectionsAsEmptyRandomizerRegistry;

public interface SerdeKosherTest<T> extends BeanieTest<T> {

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    default void testSerde(Class<? extends T> concreteClass) {
        EasyRandomParameters randomParameters = getEasyRandomParameters();
        randomParameters.randomizerRegistry(new ExcludedMapAndCollectionsAsEmptyRandomizerRegistry());
        randomParameters.randomizerRegistry(getRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T fromJson = getBeanieProvider().fromJSON(json, findRootType(concreteClass));
                        assertEquals(bean, fromJson, json);
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

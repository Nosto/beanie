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

import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.function.Supplier;

import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({"Since15", "JavaUtilDate", "UseOfObsoleteDateTimeApi", "RedundantSuppression"})
public abstract class AbstractJacksonBeanTest<T> implements VintageBeanieTest<T>
        , ConstructorParametersTest<T>
        , SerdeKosherTest<T>
        , NamingStrategyTest<T>
        , CollectionHandlingTest<T>
        , FinalPropertiesTest<T>
        , NoSettersTest<T> {

    private static final Random RANDOM = new SecureRandom();
    private static final Supplier<EasyRandomParameters> EASY_RANDOM_PARAMETERS;

    static {
        EASY_RANDOM_PARAMETERS = () -> new EasyRandomParameters()
                .collectionSizeRange(1, 3)
                .randomize(OffsetDateTime.class, () -> OffsetDateTime
                        .ofInstant(Instant.ofEpochSecond(RANDOM.nextLong() % Instant.now().getEpochSecond()), ZoneOffset.UTC))
                .randomize(java.util.Date.class, () -> new java.util.Date(RANDOM.nextLong() % Instant.now().getEpochSecond()));
    }

    private final Class<? extends T> concreteClass;

    @SuppressWarnings({"JUnitTestCaseWithNonTrivialConstructors", "unused"})
    public AbstractJacksonBeanTest(Class<? extends T> clazz) {
        this.concreteClass = clazz;
    }

    /**
     * For more information @see ConstructorParametersTest#testConstructorParameters(Class)
     */
    @Test
    public void constructorParameters() {
        testConstructorParameters(concreteClass);
    }

    /**
     * For more information @see SerdeKosherTest#testSerde(Class)
     */
    @Test
    public void serde() {
        testSerde(concreteClass);
    }

    /**
     * For more information @see NamingStrategyTest#testNamingStrategy(Class)
     */
    @Test
    public void namingStrategy() {
        testNamingStrategy(concreteClass);
    }

    /**
     * For more information @see CollectionHandlingTest#testSerdeCollection(Class)
     */
    @Test
    public void serdeCollection() {
        testSerdeCollection(concreteClass);
    }

    /**
     * For more information @see CollectionHandlingTest#testSerdeCollectionAsWell(Class)
     */
    @Test
    public void serdeCollectionAsWell() {
        testSerdeCollectionAsWell(concreteClass);
    }

    /**
     * For more information @see NoSettersTest#testNoSetters(Class)
     */
    @Test
    public void noSetters() {
        testNoSetters(concreteClass);
    }

    /**
     * For more information @see FinalPropertiesTest#testFinalProperties(Class)
     */
    @Test
    public void finalProperties() {
        testFinalProperties(concreteClass);
    }

    @Override
    public ObjectMapper getMapper() {
        return getBeanieProvider().getMapper();
    }

    @SuppressWarnings("unused")
    protected Class<? extends T> getConcreteClass() {
        return concreteClass;
    }

    @Override
    public abstract BeanieProvider getBeanieProvider();

    @Override
    public EasyRandomParameters getEasyRandomParameters() {
        return EASY_RANDOM_PARAMETERS.get();
    }
}

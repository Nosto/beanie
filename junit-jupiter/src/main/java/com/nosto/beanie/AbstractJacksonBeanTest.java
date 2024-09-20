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
import java.util.stream.Stream;

import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({"Since15", "JavaUtilDate", "UseOfObsoleteDateTimeApi", "RedundantSuppression"})
public abstract class AbstractJacksonBeanTest<T> implements JupiterBeanieTest<T>
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

    public static Stream<Class<? extends JacksonBean>> getClasses() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.nosto")));
        return reflections.getSubTypesOf(JacksonBean.class).stream();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> constructorParameters() {
        return getClasses();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> serde() {
        return getClasses();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> namingStrategy() {
        return getClasses();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> serdeCollection() {
        return getClasses();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> serdeCollectionAsWell() {
        return getClasses();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> noSetters() {
        return getClasses();
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends JacksonBean>> finalProperties() {
        return getClasses();
    }

    /**
     * For more information @see ConstructorParametersTest#testConstructorParameters(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void constructorParameters(Class<T> concreteClass) {
        testConstructorParameters(concreteClass);
    }

    /**
     * For more information @see SerdeKosherTest#testSerde(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void serde(Class<T> concreteClass) {
        testSerde(concreteClass);
    }

    /**
     * For more information @see NamingStrategyTest#testNamingStrategy(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void namingStrategy(Class<T> concreteClass) {
        testNamingStrategy(concreteClass);
    }

    /**
     * For more information @see CollectionHandlingTest#testSerdeCollection(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void serdeCollection(Class<T> concreteClass) {
        testSerdeCollection(concreteClass);
    }

    /**
     * For more information @see CollectionHandlingTest#testSerdeCollectionAsWell(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void serdeCollectionAsWell(Class<T> concreteClass) {
        testSerdeCollectionAsWell(concreteClass);
    }

    /**
     * For more information @see NoSettersTest#testNoSetters(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void noSetters(Class<T> concreteClass) {
        testNoSetters(concreteClass);
    }

    /**
     * For more information @see FinalPropertiesTest#testFinalProperties(Class)
     *
     * @param concreteClass the bean class to be tests as provided by Junit
     */
    @ParameterizedTest
    @MethodSource
    public void finalProperties(Class<T> concreteClass) {
        testFinalProperties(concreteClass);
    }

    @SuppressWarnings("unused")
    @Override
    public ObjectMapper getMapper() {
        return getBeanieProvider().getMapper();
    }

    @Override
    public abstract BeanieProvider getBeanieProvider();

    @Override
    public EasyRandomParameters getEasyRandomParameters() {
        return EASY_RANDOM_PARAMETERS.get();
    }
}

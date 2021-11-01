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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.api.RandomizerRegistry;
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.nosto.beanie.jeasy.ExcludedMapAndCollectionsAsEmptyRandomizerRegistry;
import com.nosto.beanie.jeasy.ForceAllNonPrimitivesAsNullRandomizerRegistry;

@SuppressWarnings({"JavaUtilDate", "UseOfObsoleteDateTimeApi"})
public abstract class AbstractJacksonBeanTest<T, U extends T> {

    private static final Random RANDOM = new SecureRandom();
    private static final Supplier<EasyRandomParameters> EASY_RANDOM_PARAMETERS;

    static {
        EASY_RANDOM_PARAMETERS = () -> new EasyRandomParameters()
                .collectionSizeRange(1, 3)
                .randomize(OffsetDateTime.class, () -> OffsetDateTime
                        .ofInstant(Instant.ofEpochSecond(RANDOM.nextLong() % Instant.now().getEpochSecond()), ZoneOffset.UTC))
                .randomize(java.util.Date.class, () -> new java.util.Date(RANDOM.nextLong() % Instant.now().getEpochSecond()));
    }

    private final Class<? extends T> deserClass;
    private final Class<? extends U> concreteClass;

    @SuppressWarnings({"JUnitTestCaseWithNonTrivialConstructors"})
    public AbstractJacksonBeanTest(Class<? extends U> clazz) {
        this(clazz, clazz);
    }

    public AbstractJacksonBeanTest(Class<? extends T> deserClass, Class<? extends U> concreteClass) {
        this.deserClass = deserClass;
        this.concreteClass = concreteClass;
    }

    protected RandomizerRegistry getRandomizerRegistry() {
        return new CustomRandomizerRegistry();
    }

    /**
     * Assert all properties have an equivalent {@link JsonCreator} parameter
     */
    @Test
    public void constructorParameters() {
        getDescription().findProperties()
                .stream()
                .filter(property -> Optional.ofNullable(property.getAccessor())
                        .map(prop -> !prop.hasAnnotation(JacksonOnlySerialize.class))
                        .orElse(false))
                .forEach(property -> Assert.assertTrue(String.format("Property %s of %s lacks constructor argument",
                        property.getName(), concreteClass.getName()), property.hasConstructorParameter()));
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     */
    @Test
    public void serde() {
        EasyRandomParameters randomParameters = EASY_RANDOM_PARAMETERS.get();
        randomParameters.randomizerRegistry(new ExcludedMapAndCollectionsAsEmptyRandomizerRegistry());
        randomParameters.randomizerRegistry(getRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T fromJson = getBeanieProvider().fromJSON(json, deserClass);
                        Assert.assertEquals(json, bean, fromJson);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Test that all properties of a bean are named with a consistent naming strategy.
     * If the bean is configured to use a specific naming strategy, property names should be consistent with that strategy.
     */
    @Test
    public void namingStrategy() {
        BeanDescription description = getDescription();
        getDescription().findProperties()
                .forEach(property -> verifyPropertyName(description, property));
    }

    private void verifyPropertyName(BeanDescription bean, BeanPropertyDefinition property) {
        PropertyNamingStrategy.PropertyNamingStrategyBase namingStrategy = Optional.ofNullable(bean.getClassAnnotations().get(JsonNaming.class))
                .map(JsonNaming::value)
                .filter(PropertyNamingStrategy.PropertyNamingStrategyBase.class::isAssignableFrom)
                .map(c -> {
                    try {
                        return c.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException("Cannot construct naming strategy.", e);
                    }
                })
                .map(PropertyNamingStrategy.PropertyNamingStrategyBase.class::cast)
                .orElseGet(() -> {
                    // try to detect the naming strategy
                    String name = property.getName();
                    if (name.contains("_")) {
                        return (PropertyNamingStrategy.PropertyNamingStrategyBase) PropertyNamingStrategy.SNAKE_CASE;
                    } else {
                        return new PropertyNamingStrategy.PropertyNamingStrategyBase() {
                            @Override
                            public String translate(String propertyName) {
                                return propertyName;
                            }
                        };
                    }
                });

        Assert.assertEquals(String.format("Property %s does not use strategy %s", property.getName(), namingStrategy.getClass()),
                namingStrategy.translate(property.getName()), property.getName());
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     */
    @Test
    public void serdeCollection() {
        EasyRandomParameters randomParameters = EASY_RANDOM_PARAMETERS.get();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        randomParameters.randomizerRegistry(getRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T actualObj = getBeanieProvider().fromJSON(json, deserClass);
                        T expectedObj = getBeanieProvider().fromJSON(json, deserClass);
                        Assert.assertEquals(json, expectedObj, actualObj);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     */
    @Test
    public void serdeCollectionAsWell() {
        EasyRandomParameters randomParameters = EASY_RANDOM_PARAMETERS.get();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        randomParameters.randomizerRegistry(getRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T actualObj = getBeanieProvider().fromJSON(json, deserClass);
                        List<String> properties = getDescription().findProperties()
                                .stream()
                                .filter(property -> property.getPrimaryType().isCollectionLikeType())
                                .filter(property -> property.getAccessor().getValue(actualObj) == null)
                                .filter(property -> !property.getAccessor().hasAnnotation(Nullable.class))
                                .map(BeanPropertyDefinition::getName)
                                .collect(Collectors.toList());
                        Assert.assertTrue("Following properties return null for collection types " + properties, properties.isEmpty());
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Check that there is no setters
     */
    @Test
    public void noSetters() {
        List<BeanPropertyDefinition> properties = getDescription().findProperties()
                .stream()
                .filter(BeanPropertyDefinition::hasSetter)
                .collect(Collectors.toList());
        Assert.assertTrue(String.format("Properties should be immutable but the following properties have setters: %s",
                properties
                        .stream()
                        .map(BeanPropertyDefinition::getName)
                        .collect(Collectors.joining(","))), properties.isEmpty());
    }

    /**
     * All fields should be final
     */
    @Test
    public void finalProperties() {
        List<AnnotatedField> fields = getDescription().findProperties()
                .stream()
                .flatMap(x -> Optional.ofNullable(x.getField())
                        .stream())
                .filter(x -> !Modifier.isFinal(x.getModifiers()))
                .collect(Collectors.toList());
        Assert.assertTrue(String.format("The following fields are not final: %s",
                fields.stream()
                        .map(AnnotatedField::getName)
                        .collect(Collectors.joining(", "))), fields.isEmpty());
    }

    private BeanDescription getDescription() {
        JavaType javaType = getMapper().getTypeFactory()
                .constructType(concreteClass);
        return getMapper()
                .getSerializationConfig()
                .introspect(javaType);
    }

    protected ObjectMapper getMapper() {
        return getBeanieProvider().getMapper();
    }

    @SuppressWarnings("unused")
    protected Class<? extends T> getDeserClass() {
        return deserClass;
    }

    @SuppressWarnings("unused")
    protected Class<? extends U> getConcreteClass() {
        return concreteClass;
    }

    protected abstract BeanieProvider getBeanieProvider();
}

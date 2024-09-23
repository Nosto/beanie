/*
 *  Copyright (c) 2021 Nosto Solutions Ltd All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Nosto Solutions Ltd ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into with
 *  Nosto Solutions Ltd.
 */
package com.nosto.beanie.jeasy;

import javax.annotation.Nullable;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.annotation.Exclude;
import org.junit.Assert;
import org.junit.Test;

import com.nosto.beanie.JacksonBean;

public class ForceAllNonPrimitivesAsNullRandomizerRegistryTest {

    @Test
    public void testThatTheRandomizerWorks() {
        EasyRandomParameters randomParameters = new EasyRandomParameters();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        Assert.assertNull(easyRandom.nextObject(TestBean.class).shouldBeNull);
    }

    @SuppressWarnings("all")
    private static class TestBean extends JacksonBean {

        @Nullable
        @Exclude
        public Integer shouldBeNull;
    }
}

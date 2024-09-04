# Beanie

Beanie is a simple library to sanity-check your bean ser-deser. Beanie catches a whole slew of issues such as:'

* inconsistent naming of properties e.g. mixing snake and camel cases
* mutable beans with setters
* missing constructor arguments
* issues with polymorphic serde

## Installation

Unfortunately, Beanie is not available in any public Maven repositories except the GitHub Package Registry. For more information on how to install packages
from the GitHub Package
Registry, [https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package][see the GitHub docs]

## Usage


### Usage in JUnit 4 (Vintage)

In order to use Beanie if you're using JUnit 4, you'll need to use the `junit-vintage` artifact.
In order to get the test to be picked up and run by your test engine, you will need to
include a test stub such as the one below into your test suite.

```
package com.example;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nosto.beanie.AbstractJacksonBeanTest;
import com.nosto.beanie.BeanieProvider;
import com.nosto.beanie.JacksonBean;

@SuppressWarnings("JUnitTestCaseWithNoTests")
@RunWith(Parameterized.class)
public class CheckMyBeansTest extends AbstractJacksonBeanTest<JacksonBean> {

    public CheckMyBeansTest(Class<? extends JacksonBean> deserClass, String testName) {
        super(deserClass, concreteClass);
    }

    @Override
    protected BeanieProvider getBeanieProvider() {
        return JsonMapper::new;
    }
}
```

### Usage in JUnit 5 (Jupiter)

In order to use Beanie if you're using JUnit 5, you'll need to use the `junit-jupiter` artifact. 
In order to get the test to be picked up and run by your test engine, you will need to
include a test stub such as the one below into your test suite.

```
package com.example;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nosto.beanie.BeanieProvider;
import com.nosto.beanie.JacksonBean;

public class CheckMyBeansTest extends AbstractJacksonBeanTest<JacksonBean> {

    @Override
    public BeanieProvider getBeanieProvider() {
        return JsonMapper::new;
    }
}
```

## Authors

* Olli Kuonanoja <olli@nosto.com>
* Gary Madden <gary@nosto.com>
* Mridang Agarwalla <mridang@nosto.com>

## License

MIT License

[see the GitHub docs]: https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages#installing-a-package
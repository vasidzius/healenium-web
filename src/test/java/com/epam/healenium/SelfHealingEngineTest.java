/**
 * Healenium-web Copyright (C) 2019 EPAM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epam.healenium;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SelfHealingEngineTest {

    private static final String PAGE_NAME = SelfHealingEngineTest.class.getSimpleName();

    private SelfHealingDriver driver;

    @Before
    public void createDriver() {
        WebDriver delegate = new ChromeDriver();
        driver = SelfHealingDriver.create(delegate);
    }

    @Test
    public void findNewLocatorTest() {
        driver.get("https://google.com/");
        PageAwareBy by = PageAwareBy.by(PAGE_NAME, By.xpath("//input[@name='source']"));
        WebElement input = driver.findElement(by);
        By newLocation = driver.getCurrentEngine().findNewLocations(by, driver.getPageSource()).get(0);
        Assert.assertEquals(input, driver.findElement(newLocation));
    }

    @Test
    public void locatorHealingTest() {
        final By inputFieldLocator = By.xpath("//input[@name='source']");
        driver.get("https://google.com/");
        PageAwareBy by = PageAwareBy.by(PAGE_NAME, inputFieldLocator);
        WebElement input = driver.findElement(by);
        JavascriptExecutor js = driver.getDelegate();
        js.executeScript("arguments[0].setAttribute('name', 'source_new')", input);
        by = PageAwareBy.by(PAGE_NAME, inputFieldLocator);
        input = driver.findElement(by);
        By newLocation = driver.getCurrentEngine().findNewLocations(by, driver.getPageSource()).get(0);
        Assert.assertEquals(input, driver.findElement(newLocation));
    }

    @After
    public void close() {
        driver.quit();
    }
}

package org.jboss.arquillian.graphene.performance;

import java.util.List;
import junit.framework.Assert;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
public class SimpleTest {

    private static final long SIZE = 100;
    private static final long NUMBER = 50;

    @FindBy(id="number")
    private WebElement number;
    @FindBy(id="generate")
    private WebElement generate;

    @FindBy(css="#items div")
    private List<WebElement> cssItems;
    @FindBy(css="#items div")
    private WebElement cssItem;
    @FindBy(jquery="#items div")
    private List<WebElement> jqueryItems;
    @FindBy(jquery="#items div")
    private WebElement jqueryItem;

    @Drone
    private WebDriver browser;

    @BeforeClass
    public static void beginInformation() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("  size: " + SIZE);
        System.out.println("  number: " + NUMBER);
        System.out.println("--------------------------------------------------------------------------------");
    }

    @AfterClass
    public static void endInformation() {
        System.out.println("--------------------------------------------------------------------------------");
    }

    @Test
    public void testStaticPureElement() {
        prepare(1);
        final WebElement pure = (WebElement) ((GrapheneProxyInstance) cssItem).unwrap();
        measureItem("static pure element", new ConstTarget<WebElement>(pure), 0);
    }

    @Test
    public void testDynamicPureElement() {
        prepare(1);
        final SearchContext searchContext = (SearchContext) ((GrapheneProxyInstance) browser).unwrap();
        final By by = By.cssSelector("#items div");
        measureItem(
                "dynamic pure element",
                new Target<WebElement>() {
                    public WebElement getTarget() {
                        return searchContext.findElement(by);
                    }
                },
                0);
    }

    @Test
    public void testInjectedCssElement() {
        prepare(1);
        measureItem("injected css element", new ConstTarget<WebElement>(cssItem),0);
    }

    @Test
    public void testInjectedJQueryElement() {
        prepare(1);
        measureItem("injected jquery element", new ConstTarget<WebElement>(jqueryItem), 0);
    }

    @Test
    public void testStaticPureElements() {
        prepare(NUMBER);
        List<WebElement> pure = ((WebDriver)((GrapheneProxyInstance) browser).unwrap()).findElements(By.cssSelector("#items div"));
        measureList("static pure elements", new ConstTarget<List<WebElement>>(pure), NUMBER);
    }

    @Test
    public void testDynamicPureElements() {
        prepare(NUMBER);
        final SearchContext searchContext = (SearchContext) ((GrapheneProxyInstance) browser).unwrap();
        final By by = By.cssSelector("#items div");
        measureList(
                "dynamic pure elements",
                new Target<List<WebElement>>() {
                    public List<WebElement> getTarget() {
                        return searchContext.findElements(by);
                    }
                },
                NUMBER);
    }

    @Test
    public void testInjectedCssElements() {
        prepare(NUMBER);
        measureList("injected css elements", new ConstTarget<List<WebElement>>(cssItems), NUMBER);
    }

    @Test
    public void testInjectedJqueryElements() {
        prepare(NUMBER);
        measureList("injected jquery elements", new ConstTarget<List<WebElement>>(jqueryItems), NUMBER);
    }

    protected void measureItem(String measurement, Target<WebElement> item, long n) {
        long before = System.currentTimeMillis();
        for (long i=0; i<SIZE; i++) {
            checkItem(item.getTarget(), n);
        }
        long after = System.currentTimeMillis();
        System.out.println("### measurement [" + measurement + "]: " + (after - before) + " ms");
    }

    protected void measureList(String measurement, Target<List<WebElement>> items, long expectedSize) {
        Assert.assertEquals(expectedSize, items.getTarget().size());
        long before = System.currentTimeMillis();
        for (long i=0; i<SIZE; i++) {
            checkList(items.getTarget());
        }
        long after = System.currentTimeMillis();
        System.out.println("### measurement [" + measurement + "]: " + (after - before) + " ms");
    }

    protected void checkItem(WebElement item, long n) {
        Assert.assertEquals("The item text doesn't match.", "item-" + n, item.getText().trim());
    }

    protected void checkList(List<WebElement> items) {
        long i=0;
        for (WebElement item: items) {
            checkItem(item, i);
            i++;
        }
    }

    protected void prepare(long n) {
        browser.get(this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/performance/index.html").toString());
        number.click();
        number.clear();
        number.sendKeys(Long.toString(n));
        generate.click();
    }

    public static class ConstTarget<T> implements Target<T> {

        private final T target;

        public ConstTarget(T target) {
            this.target = target;
        }

        @Override
        public T getTarget() {
            return target;
        }

    }

    public interface Target<T> {

        T getTarget();

    }

}

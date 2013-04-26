# Graphene Performance Tests

## Description

Performance tests for [Graphene Webdriever](https://github.com/arquillian/arquillian-graphene)

### One Element Test

Test reapeatedly call `element.getText()`.

* **static pure element**: the element is found firstly and then the test is performed
* **dynamic pure element**: the element is foud in earch iteration
* **injected css element**: the element injected by Graphene (CSS locator) is used
* **injected jquery element**: the element injected by Graphene (JQuery locator) is used

### List of Elements Test

Test reapeatedly call `for (Element element: elements) element.getText()`.

* **static pure elements**: the list of elements is found firstly and then the test is performed
* **dynamic pure elements**: the list of elements is foud in earch iteration
* **injected css elements**: the list of elements injected by Graphene (CSS locator) is used
* **injected jquery elements**: the list of elements injected by Graphene (JQuery locator) is used

## Results (2013-04-26)

### One Element Test

![Firefox](/done/2013-04-26/graphs/firefox-element.png)
![Google Chrome](/done/2013-04-26/graphs/chrome-element.png)
![HtmlUnit](/done/2013-04-26/graphs/htmlunit-element.png)
![PhantomJS](/done/2013-04-26/graphs/phantomjs-element.png)

### List of Elements Test

![Firefox](/done/2013-04-26/graphs/firefox-elements.png)
![Google Chrome](/done/2013-04-26/graphs/chrome-elements.png)
![HtmlUnit](/done/2013-04-26/graphs/htmlunit-elements.png)
![PhantomJS](/done/2013-04-26/graphs/phantomjs-elements.png)

package com.example.sitodo.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("User Story 3: See Motivation Message")
@Tag("e2e")
class SeeMotivationMessageTest extends BaseFunctionalTest {

    private static final Logger LOG = LoggerFactory.getLogger(SeeMotivationMessageTest.class);

    @Value("${sitodo.motivation.empty}")
    private String emptyListMessage;

    @Value("${sitodo.motivation.noFinished}")
    private String noFinishedMessage;

    @Value("${sitodo.motivation.halfFinished}")
    private String halfFinishedMessage;

    @Value("${sitodo.motivation.someFinished}")
    private String someFinishedMessage;

    @Value("${sitodo.motivation.allFinished}")
    private String allFinishedMessage;

    @Value("${sitodo.motivation.fewItems}")
    private String fewItemsMessage;

    @Value("${sitodo.motivation.manyItems}")
    private String manyItemsMessage;

    @Value("${sitodo.motivation.fewItemsThreshold:5}")
    private int fewItemsThreshold;

    @Value("${sitodo.motivation.manyItemsThreshold:10}")
    private int manyItemsThreshold;

    @Test
    @DisplayName("A user can see a motivation message when the list is empty")
    void emptyTodoList_showMessage() throws Exception {
        driver.get(createBaseUrl("localhost", serverPort));

        WebElement motivationMessage = driver.findElement(By.id("motivation_message"));

        assertThat(motivationMessage.getText(), containsString(emptyListMessage));
    }

    @Test
    @DisplayName("A user can see a motivation message when there is only one item in the list")
    void todoList_singleItem() throws Exception {
        driver.get(createBaseUrl("localhost", serverPort));

        // Add one item
        postNewTodoItem("Buy milk");

        // See the motivation message when there is no finished item
        WebElement motivationMessage = driver.findElement(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(fewItemsMessage),
            containsString(noFinishedMessage)
        ));

        // Finish that one item
        WebElement markFinishLink = driver.findElement(By.className("sitodo-finish-link"));
        markFinishLink.click();

        // The message should be different
        motivationMessage = driver.findElement(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(fewItemsMessage),
            containsString(allFinishedMessage)
        ));
    }

    @Test
    @DisplayName("A user can see a motivation message when half of the items are finished")
    void todoList_multipleItems_halfFinished() throws Exception {
        driver.get(createBaseUrl("localhost", serverPort));

        // Create 10 items
        List<String> items = IntStream.range(0, manyItemsThreshold)
            .mapToObj(i -> "Task " + i)
            .toList();

        items.forEach(item -> {
            postNewTodoItem(item);

            try {
                // Introduce artificial delay allowing DOM to be correctly rendered after inserting
                // multiple items consecutively
                Thread.sleep(500);
            } catch (InterruptedException exception) {
                LOG.error("There was a problem during artificial delay", exception);
            }
        });

        // Check initial motivation message
        WebElement motivationMessage = driver.findElement(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(manyItemsMessage),
            containsString(noFinishedMessage)
        ));

        // Mark half of the items as finished
        IntStream.range(0, items.size() / 2).forEach(i ->
            new WebDriverWait(driver, DEFAULT_WAIT)
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(By.tagName("tbody"), By.tagName("tr")))
                .stream()
                .filter(row -> {
                    List<WebElement> columns = row.findElements(By.tagName("td"));
                    WebElement statusColumn = columns.get(columns.size() - 2);

                    return statusColumn.getText().equalsIgnoreCase("Not Finished");
                })
                .findFirst()
                .ifPresentOrElse(
                    row -> {
                        List<WebElement> columns = row.findElements(By.tagName("td"));
                        WebElement linkColumn = columns.get(3);
                        WebElement link = linkColumn.findElement(By.tagName("a"));

                        link.click();
                    }, () -> fail("There was no clickable link in the todo item")
                )
        );

        // Check final motivation message
        motivationMessage = driver.findElement(By.id("motivation_message"));
        assertThat(motivationMessage.getText(), allOf(
            containsString(manyItemsMessage),
            containsString(halfFinishedMessage)
        ));
    }

    // The motivation message is not a separate user story. It is actually
    // a side-effect that experienced by user when adding and updating the
    // items. Considering the cost of functional (Selenium) tests in terms
    // of test execution speed and complexity, it might be better if the
    // motivation message is verified alongside with the actions performed
    // during adding and updating the items.
}

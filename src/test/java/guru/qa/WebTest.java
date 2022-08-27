package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;
import static com.codeborne.selenide.Condition.text;

import static com.codeborne.selenide.Selenide.*;

public class WebTest {
    @BeforeAll
    static void configure() {
        Configuration.browserSize = "1920x1080";
        Configuration.browser = "chrome";
    }

    @ValueSource(strings = {"Стул"})
    @ParameterizedTest
    void commonSearchTest(String testData) {
        open("https://www.ikea.com/ru/ru/");
        $(".search-field__input").setValue(testData).pressEnter();
        $(".header-section__title").shouldHave(text("ADDE АДДЕ"));
    }

    @CsvSource(value = {
            "Стул, Найдено 415 товаров и 101 статья",
            "Диван,  Найдено 272 товаров и 28 статей",
    })
    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса: \"{0}\"")
    void commonComplexSearchTest(String testData, String expectedResult) {
        open("https://www.ikea.com/ru/ru/");
        $(".search-field__input").setValue(testData).pressEnter();
        $$(".search-summary__message").shouldHave(CollectionCondition.itemWithText(expectedResult));
    }


    static Stream<Arguments> dataProviderForSelenideSiteMenuTest() {
        return Stream.of(
                Arguments.of("Стул", List.of("Сортировать \r Категория \r Цвет \r  Цена \r Материал  \r Размер \r Свойства")),
                Arguments.of("Диван", List.of("Сортировать \n Цена \n Размер \n Цвет \n Форма \n Количество мест \n Категория"))
        );
    }

    @MethodSource("dataProviderForSelenideSiteMenuTest")
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки {1}")
    void selenideSiteMenuTest(String furniture, List<String> expectedButtons) {
        open("https://selenide.org/");
        open("https://www.ikea.com/ru/ru/");
        $(".search-field__input").setValue(furniture).pressEnter();
        $$(".filter-options").shouldHave(CollectionCondition.texts(expectedButtons));
    }

}

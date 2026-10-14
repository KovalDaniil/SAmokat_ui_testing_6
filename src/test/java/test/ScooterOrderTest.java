package test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.ScooterPage;
import test.data.OrderData;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Функциональность: заказ самоката (позитивный сценарий).
 * 1. Нажать кнопку «Заказать» - на странице две кнопки, проверяем обе точки входа.
 * 2. Заполнить форму «Для кого самокат».
 * 3. Заполнить форму «Про аренду».
 * 4. Нажать «Заказать» и подтвердить заказ.
 * 5. Проверить всплывающее окно об успешном создании заказа.
 * Два набора данных передаются через @MethodSource.
 */
public class ScooterOrderTest extends BaseTest {

    public static Stream<Arguments> ordersData() {
        OrderData ivan = new OrderData(
                "Иван", "Иванов", "Ленинский проспект, 1", "79991234567",
                "15.11.2026", "сутки", "черный", "Привезите после 18:00");
        OrderData maria = new OrderData(
                "Мария", "Петрова", "Тверская, 3", "79997654321",
                "20.11.2026", "четверо суток", "серый", "Оставьте у консьержа");
        return Stream.of(Arguments.of(ivan), Arguments.of(maria));
    }
    /*
     * Полный позитивный флоу через кнопку «Заказать».
     * баг приложения: в Chrome после подтверждения заказа окно
     * об успехе не появляется, в Chrome этот тест падает, в Firefox нет.
     */
    @ParameterizedTest(name = "Заказ для {0}")
    @MethodSource("ordersData")
    public void testFullOrderFlowFromTopButton(OrderData order) {
        ScooterPage scooterPage = new ScooterPage(driver);
        scooterPage.open();

        scooterPage.clickOrderUpButton();
        scooterPage.fillForWhomForm(order);
        scooterPage.clickNext();
        scooterPage.fillAboutRentForm(order);
        scooterPage.clickPlaceOrderButton();
        scooterPage.acceptOrderPrompt();

        assertTrue(scooterPage.isOrderSuccessPopupVisible(),
                "Не появилось всплывающее окно с сообщением об успешном создании заказа");
    }

    /* Тот же флоу, но вход через нижнюю кнопку «Заказать» в футере страницы. */
    @ParameterizedTest(name = "Заказ снизу для {0}")
    @MethodSource("ordersData")
    public void testFullOrderFlowFromBottomButton(OrderData order) {
        ScooterPage scooterPage = new ScooterPage(driver);
        scooterPage.open();

        scooterPage.clickOrderDownButton();
        scooterPage.fillForWhomForm(order);
        scooterPage.clickNext();
        scooterPage.fillAboutRentForm(order);
        scooterPage.clickPlaceOrderButton();
        scooterPage.acceptOrderPrompt();

        assertTrue(scooterPage.isOrderSuccessPopupVisible(),
                "Не появилось всплывающее окно с сообщением об успешном создании заказа");
    }

    /* Точки входа: после клика по верхней или нижней кнопке открывается форма заказа. */
    @ParameterizedTest(name = "Точка входа: кнопка {0}")
    @MethodSource("entryPoints")
    public void testEntryPointOpensOrderForm(String buttonName) {
        ScooterPage scooterPage = new ScooterPage(driver);
        scooterPage.open();

        if (buttonName.equals("верхняя")) {
            scooterPage.clickOrderUpButton();
        } else {
            scooterPage.clickOrderDownButton();
        }
        assertTrue(scooterPage.isForWhomFormOpened(), "Форма «Для кого самокат» не открылась");
    }
    public static Stream<Arguments> entryPoints() {
        return Stream.of(Arguments.of("верхняя"), Arguments.of("нижняя"));
    }
}

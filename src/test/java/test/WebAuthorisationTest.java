package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

public class WebAuthorisationTest {

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("h2").shouldBe(Condition.appear, Duration.ofSeconds(15)).shouldHave(Condition.ownText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.ownText("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.ownText("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.ownText("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $$("button").find(Condition.exactText("Продолжить")).click();
        $("[data-test-id='error-notification']").shouldBe(Condition.appear, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.ownText("Неверно указан логин или пароль"));
    }
}
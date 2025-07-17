package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SimpleUserService;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private UserController userController;
    private MockHttpSession httpSession;
    private MockHttpServletRequest request;

    @BeforeEach
    public void initService() {
        userService = mock(SimpleUserService.class);
        userController = new UserController(userService);
        httpSession = new MockHttpSession();
        request = new MockHttpServletRequest();
    }

    @Test
    public void whenGetLoginPageThenReturnLoginPage() {
        var view = userController.getLogin();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenGetRegisterPageThenReturnRegisterPage() {
        var view = userController.getRegisterPage();
        assertThat(view).isEqualTo("users/create");
    }

    @Test
    public void whenGetLogoutPageThenReturnLogoutPage() {
        var view = userController.logout(httpSession);
        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void whenRegisterUserThenReturnRegisteredUser() {
        Optional<User> registeredUser = Optional.of(new User("aleks", "em@ail.com", "12345"));
        when(userService.save(registeredUser.get())).thenReturn(registeredUser);
        var model = new ConcurrentModel();
        var view = userController.register(model, registeredUser.get());
        assertThat(view).isEqualTo("redirect:/sessions");
    }

    @Test
    public void whenRegisterUserWithNotUnicEmailThenThrowException() {
        Optional<User> registeredUser = Optional.of(new User("aleks", "em@ail.com", "12345"));
        var expectedException = new RuntimeException("Пользователь с такой почтой уже существует");
        when(userService.save(registeredUser.get())).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = userController.register(model, registeredUser.get());
        var actualException = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualException).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenLoginUserThenReturnOk() {
        Optional<User> loginUser = Optional.of(new User("aleks", "em@ail.com", "12345"));
        when(userService.findByEmailAndPassword(loginUser.get())).thenReturn(loginUser);
        var model = new ConcurrentModel();
        var view = userController.login(model, loginUser.get(), request);
        var actualLoginUser = request.getSession().getAttribute("user");
        assertThat(view).isEqualTo("redirect:/sessions");
        assertThat(actualLoginUser).isEqualTo(loginUser.get());
    }

    @Test
    public void whenLoginUserAndFailedItThenThrowError() {
        Optional<User> loginUser = Optional.of(new User("aleks", "em@ail.com", "12345"));
        var expectedError = new RuntimeException("Такой пользователь не существует");
        when(userService.findByEmailAndPassword(loginUser.get())).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = userController.login(model, loginUser.get(), request);
        var actualErorr = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualErorr).isEqualTo(expectedError.getMessage());
    }
}
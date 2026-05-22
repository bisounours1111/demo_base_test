package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void shouldValidatePassword() {
        assertTrue(validator.isValid("Password1!"));
        assertTrue(validator.isValid("Admin2024@"));
    }

    @Test
    void shouldRejectNullPassword() {
        assertFalse(validator.isValid(null));
        assertEquals(
                "Password must not be null",
                validator.getErrorMessage(null)
        );
    }

    @Test
    void shouldRejectShortPassword() {
        assertFalse(validator.isValid("short1!"));

        assertEquals(
                "Password must contain at least 8 characters",
                validator.getErrorMessage("short1!")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "Password1!, true",
            "Admin2024@, true",
            "short1!, false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!, false",
            "Password1, false"
    })
    void shouldValidatePasswords(String password, boolean expected) {

        assertEquals(expected, validator.isValid(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PASSWORD1!",
            "HELLO123@",
            "JAVA2025#"
    })
    void shouldRejectPasswordsWithoutLowercase(String password) {

        assertEquals(
                "Password must contain at least one lowercase letter",
                validator.getErrorMessage(password)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordProvider")
    void shouldReturnCorrectMessages(
            String password,
            String expectedMessage
    ) {

        assertEquals(
                expectedMessage,
                validator.getErrorMessage(password)
        );
    }

    static Stream<Arguments> invalidPasswordProvider() {

        return Stream.of(

                Arguments.of(
                        "PASSWORD1!",
                        "Password must contain at least one lowercase letter"
                ),

                Arguments.of(
                        "password1!",
                        "Password must contain at least one uppercase letter"
                ),

                Arguments.of(
                        "Password!",
                        "Password must contain at least one digit"
                ),

                Arguments.of(
                        "Password1",
                        "Password must contain at least one special character"
                )
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullAndEmptyPasswords(String password) {

        if (password == null) {

            assertEquals(
                    "Password must not be null",
                    validator.getErrorMessage(password)
            );

        } else {

            assertEquals(
                    "Password must contain at least 8 characters",
                    validator.getErrorMessage(password)
            );
        }
    }
}

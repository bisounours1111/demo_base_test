package com.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradingCalculatorTest {

    @ParameterizedTest(name = "score={0}%, présence={1}% => note {2}")
    @CsvSource({
            "95, 90, A",
            "85, 90, B",
            "65, 90, C",
            "95, 65, B",
            "95, 55, F",
            "65, 55, F",
            "50, 90, F"
    })
    void shouldReturnExpectedGrade(int score, int attendancePercentage, char expectedGrade) {
        GradingCalculator calculator = new GradingCalculator(score, attendancePercentage);

        char grade = calculator.getGrade();

        assertEquals(expectedGrade, grade);
    }
}

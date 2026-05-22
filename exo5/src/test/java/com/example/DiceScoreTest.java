package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiceScoreTest {

    @Mock
    private Ide de;

    private DiceScore diceScore;

    @BeforeEach
    void setUp() {
        diceScore = new DiceScore(de);
    }

    @Test
    void shouldReturnDoublePlusTenWhenBothRollsAreEqual() {
        when(de.getRoll()).thenReturn(4, 4);

        int result = diceScore.getScore();

        assertEquals(18, result);
        verify(de, times(2)).getRoll();
    }

    @Test
    void shouldReturnThirtyWhenBothRollsAreSix() {
        when(de.getRoll()).thenReturn(6, 6);

        int result = diceScore.getScore();

        assertEquals(30, result);
        verify(de, times(2)).getRoll();
    }

    @Test
    void shouldReturnHighestRollWhenRollsAreDifferent() {
        when(de.getRoll()).thenReturn(2, 5);

        int result = diceScore.getScore();

        assertEquals(5, result);
        verify(de, times(2)).getRoll();
    }

    @Test
    void shouldReturnHighestRollRegardlessOfOrder() {
        when(de.getRoll()).thenReturn(5, 2);

        int result = diceScore.getScore();

        assertEquals(5, result);
        verify(de, times(2)).getRoll();
    }
}

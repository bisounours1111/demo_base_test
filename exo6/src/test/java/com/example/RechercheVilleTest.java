package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RechercheVilleTest {

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        rechercheVille = new RechercheVille();
    }

    @Nested
    class Etape1 {

        @Test
        void shouldThrowNotFoundExceptionWhenSearchTextIsEmpty() {
            assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher(""));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenSearchTextHasOneCharacter() {
            assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher("V"));
        }

        @Test
        void shouldReturnCitiesStartingWithSearchText() throws NotFoundException {
            List<String> result = rechercheVille.Rechercher("Va");

            assertEquals(List.of("Valence", "Vancouver"), result);
        }

        @Test
        void shouldBeCaseInsensitive() throws NotFoundException {
            List<String> result = rechercheVille.Rechercher("va");

            assertEquals(List.of("Valence", "Vancouver"), result);
        }
    }

    @Nested
    class Etape2 {

        @Test
        void shouldReturnCityWhenSearchTextIsPartOfName() throws NotFoundException {
            List<String> result = rechercheVille.Rechercher("ape");

            assertEquals(List.of("Budapest"), result);
        }

        @Test
        void shouldReturnAllCitiesWhenSearchTextIsAsterisk() throws NotFoundException {
            List<String> result = rechercheVille.Rechercher("*");

            assertEquals(Arrays.asList(
                    "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
                    "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
                    "Hong Kong", "Dubaï", "Rome", "Istanbul"
            ), result);
        }
    }
}

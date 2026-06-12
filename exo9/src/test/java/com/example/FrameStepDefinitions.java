package com.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class FrameStepDefinitions {

    private IGenerateur generateur;
    private Frame frame;
    private boolean lastRollAccepted;

    @Before
    public void reset() {
        generateur = Mockito.mock(IGenerateur.class);
        frame = null;
        lastRollAccepted = false;
    }

    @Given("une série standard")
    public void uneSerieStandard() {
        frame = new Frame(generateur, false);
    }

    @Given("une série finale")
    public void uneSerieFinale() {
        frame = new Frame(generateur, true);
    }

    @Given("le lancer sur {int} quilles retourne {int}")
    public void leLancerRetourne(int max, int pins) {
        when(generateur.randomPin(max)).thenReturn(pins);
    }

    @Given("le lancer sur {int} quilles retourne {int} puis {int}")
    public void leLancerRetournePuis(int max, int first, int second) {
        when(generateur.randomPin(max)).thenReturn(first, second);
    }

    @Given("le lancer sur {int} quilles retourne {int} puis {int} puis {int}")
    public void leLancerRetournePuisTrois(int max, int first, int second, int third) {
        when(generateur.randomPin(max)).thenReturn(first, second, third);
    }

    @When("le joueur effectue un lancer")
    public void leJoueurEffectueUnLancer() {
        lastRollAccepted = frame.makeRoll();
    }

    @Then("le lancer est accepté")
    public void leLancerEstAccepte() {
        assertTrue(lastRollAccepted);
    }

    @Then("le lancer est refusé")
    public void leLancerEstRefuse() {
        assertFalse(lastRollAccepted);
    }

    @Then("le score de la série est {int}")
    public void leScoreDeLaSerieEst(int expectedScore) {
        assertEquals(expectedScore, frame.getScore());
    }
}

package com.example.demoapi.bdd;

import com.example.demoapi.model.Reservation;
import com.example.demoapi.model.ReservationStatus;
import com.example.demoapi.repository.ReservationRepository;
import com.example.demoapi.repository.RoomRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoomReservationApiStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ResultActions lastResult;

    @Given("no room or reservation exists in the API")
    public void noRoomNorReservationExists() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Given("a room {string} with capacity {int} exists in the API")
    public void roomExists(String name, int capacity) {
        roomRepository.save(name, capacity);
    }

    @Given("a confirmed reservation exists for room {long} by {string} from {string} to {string}")
    public void confirmedReservationExists(Long roomId, String reservedBy, String startTime, String endTime) {
        reservationRepository.save(new Reservation(
                null,
                roomId,
                reservedBy,
                Instant.parse(startTime),
                Instant.parse(endTime),
                ReservationStatus.CONFIRMED
        ));
    }

    @When("I create a reservation for room {long} by {string} from {string} to {string}")
    public void createReservation(Long roomId, String reservedBy, String startTime, String endTime) throws Exception {
        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "roomId": %d,
                          "reservedBy": "%s",
                          "startTime": "%s",
                          "endTime": "%s"
                        }
                        """.formatted(roomId, reservedBy, startTime, endTime)));
    }

    @Then("the HTTP response should be {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("the response contains reservedBy {string}")
    public void responseShouldContainReservedBy(String expectedReservedBy) throws Exception {
        lastResult.andExpect(jsonPath("$.reservedBy").value(expectedReservedBy));
    }

    @And("the response contains status {string}")
    public void responseShouldContainStatus(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @And("the response contains an error message")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}

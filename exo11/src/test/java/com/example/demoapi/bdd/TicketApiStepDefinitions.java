package com.example.demoapi.bdd;

import com.example.demoapi.model.Priority;
import com.example.demoapi.model.TicketStatus;
import com.example.demoapi.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketApiStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long createdTicketId;

    @Given("no ticket exists in the API")
    public void noTicketExists() {
        repository.deleteAll();
        createdTicketId = null;
    }

    @Given("the following tickets exist in the API")
    public void followingTicketsExist(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            String title = row.get("title");
            Priority priority = Priority.valueOf(row.get("priority"));
            repository.save(title, priority, TicketStatus.OPEN);
        }
    }

    @When("I create a ticket with title {string} and priority {string}")
    public void createTicketWithTitleAndPriority(String title, String priority) throws Exception {
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + title + "\",\"priority\":\"" + priority + "\"}"));

        var responseBody = lastResult.andReturn().getResponse().getContentAsString();
        if (!responseBody.isBlank() && lastResult.andReturn().getResponse().getStatus() == 201) {
            createdTicketId = objectMapper.readTree(responseBody).get("id").asLong();
        }
    }

    @When("I create a ticket with title {string} and no priority")
    public void createTicketWithoutPriority(String title) throws Exception {
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + title + "\"}"));
    }

    @When("I update the created ticket status to {string}")
    public void updateCreatedTicketStatus(String status) throws Exception {
        lastResult = mockMvc.perform(patch("/api/tickets/" + createdTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"" + status + "\"}"));
    }

    @When("I request the created ticket")
    public void getCreatedTicket() throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + createdTicketId));
    }

    @When("I request the ticket with id {long}")
    public void getTicketById(Long id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
    }

    @When("I request the ticket list")
    public void getAllTickets() throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets"));
    }

    @Then("the HTTP response should be {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("the response contains title {string}")
    public void responseShouldContainTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Then("the response contains priority {string}")
    public void responseShouldContainPriority(String expectedPriority) throws Exception {
        lastResult.andExpect(jsonPath("$.priority").value(expectedPriority));
    }

    @Then("the response contains status {string}")
    public void responseShouldContainStatus(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @Then("the response contains an error message")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }

    @Then("the response contains {int} tickets")
    public void responseShouldContainTicketCount(int expectedCount) throws Exception {
        lastResult.andExpect(jsonPath("$.length()").value(expectedCount));
    }

    @Then("the response contains at least title {string}")
    public void responseShouldContainAtLeastTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$[*].title", hasItem(expectedTitle)));
    }
}

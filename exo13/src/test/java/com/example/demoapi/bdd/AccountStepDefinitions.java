package com.example.demoapi.bdd;

import com.example.demoapi.model.Account;
import com.example.demoapi.repository.AccountRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    private ResultActions lastResult;

    @Given("no account exists in the API")
    public void noAccountExists() {
        repository.deleteAll();
    }

    @Given("account {string} owned by {string} exists with a balance of {int}")
    public void accountExistsWithBalance(String number, String holder, int balance) {
        repository.save(number, holder);
        if (balance != 0) {
            repository.update(new Account(number, holder, new BigDecimal(balance)));
        }
    }

    @When("I create account {string} for {string}")
    public void createAccount(String number, String holder) throws Exception {
        lastResult = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"number":"%s","holder":"%s"}
                        """.formatted(number, holder)));
    }

    @When("I deposit {int} on account {string}")
    public void deposit(int amount, String number) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/{number}/deposit", number)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount":%d}
                        """.formatted(amount)));
    }

    @When("I withdraw {int} from account {string}")
    public void withdraw(int amount, String number) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/{number}/withdraw", number)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"amount":%d}
                        """.formatted(amount)));
    }

    @When("I transfer {int} from account {string} to account {string}")
    public void transfer(int amount, String fromAccount, String toAccount) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fromAccount": "%s",
                          "toAccount": "%s",
                          "amount": %d
                        }
                        """.formatted(fromAccount, toAccount, amount)));
    }

    @Then("the HTTP response should be {int}")
    public void httpResponseShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @And("account {string} should belong to {string}")
    public void accountShouldBelongTo(String number, String holder) throws Exception {
        lastResult.andExpect(jsonPath("$.number").value(number));
        lastResult.andExpect(jsonPath("$.holder").value(holder));
    }

    @And("account {string} should have a balance of {int}")
    public void accountShouldHaveBalance(String number, int expectedBalance) throws Exception {
        mockMvc.perform(get("/accounts/{number}", number))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance));
    }

    @And("the response should contain an error message")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}

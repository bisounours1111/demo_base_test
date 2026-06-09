package com.example;

import com.example.domain.CustomerProfile;
import com.example.domain.Order;
import com.example.domain.OrderReceipt;
import com.example.domain.Product;
import com.example.exception.OrderRefusedException;
import com.example.repository.ProductRepository;
import com.example.service.OrderService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStepDefinitions {

    private final ProductRepository productRepositoryMock = Mockito.mock(ProductRepository.class);
    private final OrderService orderService = new OrderService(productRepositoryMock);

    private CustomerProfile currentCustomerProfile;
    private OrderReceipt generatedReceipt;
    private Exception thrownException;
    private String lastProductReference;
    private int lastOrderQuantity;

    @Given("a product with reference {string}, a price of {double} and a stock of {int}")
    public void aProductExists(String ref, double price, int stock) {
        Product mockProduct = new Product(ref, "Test Product", price, stock);
        Mockito.when(productRepositoryMock.findByReference(ref))
                .thenReturn(Optional.of(mockProduct));
    }

    @Given("an unknown product with reference {string}")
    public void anUnknownProduct(String ref) {
        Mockito.when(productRepositoryMock.findByReference(ref))
                .thenReturn(Optional.empty());
    }

    @Given("a customer with profile {string}")
    public void aCustomerWithProfile(String profile) {
        this.currentCustomerProfile = CustomerProfile.valueOf(profile);
    }

    @When("the customer orders a quantity of {int} for reference {string}")
    public void theCustomerOrders(int quantity, String ref) {
        this.lastProductReference = ref;
        this.lastOrderQuantity = quantity;
        Order order = new Order("customer@test.com", ref, quantity);
        try {
            this.generatedReceipt = orderService.placeOrder(order, currentCustomerProfile);
            this.thrownException = null;
        } catch (Exception e) {
            this.thrownException = e;
            this.generatedReceipt = null;
        }
    }

    @Then("the order is accepted and the receipt shows a total amount of {double}")
    public void theOrderIsAccepted(double expectedAmount) {
        assertNull(thrownException, "The order was unexpectedly refused.");
        assertNotNull(generatedReceipt, "The order receipt was not generated.");
        assertEquals(expectedAmount, generatedReceipt.getTotalAmount(), 0.01);
        assertEquals(lastProductReference, generatedReceipt.getProductReference());
        assertEquals(lastOrderQuantity, generatedReceipt.getQuantity());
        assertEquals(
                "Order confirmed for customer@test.com - Test Product",
                generatedReceipt.getConfirmationMessage()
        );
    }

    @Then("the order is refused with the message {string}")
    public void theOrderIsRefused(String expectedErrorMessage) {
        assertNull(generatedReceipt, "The order was unexpectedly accepted.");
        assertNotNull(thrownException, "No exception was thrown.");
        assertInstanceOf(OrderRefusedException.class, thrownException);
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }
}
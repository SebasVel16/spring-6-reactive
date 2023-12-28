package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.CustomerDTO;
import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.domain.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder().name("SebastianTest").email("test@mail.com").build();
    }

    @Test
    void listCustomers() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type","application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(2);
    }

    @Test
    void getCustomerById() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID,1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type","application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void getCustomerByIdNotFound() {
        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID,990)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createNewCustomer() {
        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(customer),CustomerDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/3");
    }

    @Test
    void createNewCustomerValidationError() {
        customer.setEmail("");
        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(customer),CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomer() {
        webTestClient.put()
                .uri(CustomerController.CUSTOMER_PATH_ID,1)
                .body(Mono.just(customer),CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void updateCustomerNotFound() {
        webTestClient.put()
                .uri(CustomerController.CUSTOMER_PATH_ID,999)
                .body(Mono.just(customer),CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateCustomerValidationError() {
        customer.setName("");
        webTestClient.put()
                .uri(CustomerController.CUSTOMER_PATH_ID,1)
                .body(Mono.just(customer),CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deleteCustomer() {
        webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID,1)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void deleteCustomerNotFound() {
        webTestClient.delete().uri(CustomerController.CUSTOMER_PATH_ID,999)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
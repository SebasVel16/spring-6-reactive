package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.domain.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    Beer beer;

    @BeforeEach
    void setUp() {
         beer = Beer.builder().upc("testUpc").beerStyle("testStyle").beerName("beerTest")
                .quantityOnHand(3).price(BigDecimal.TEN).lastModifiedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now()).build();
    }

    @Test
    void listBeers() {
        webTestClient.get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type","application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    void getBeerById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID,1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type","application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void getBeerByIdNotFound() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID,999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createNewBeer() {
        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(beer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    void createNewBeerValidationError() {
        beer.setBeerName("");
        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(beer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void updateBeer() {
        webTestClient.put()
                .uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(beer), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void updateBeerNotFound() {
        webTestClient.put()
                .uri(BeerController.BEER_PATH_ID, 999)
                .body(Mono.just(beer), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateBeerValidationError() {
        beer.setBeerName("");
        webTestClient.put()
                .uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(beer), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deleteBeer() {
        webTestClient.delete()
                .uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void deleteBeerNotFound() {
        webTestClient.delete()
                .uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
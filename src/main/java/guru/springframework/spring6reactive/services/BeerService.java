package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.domain.BeerDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDTO> listAll();

    Mono<BeerDTO> getById(Integer beerId);

    Mono<BeerDTO> save(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO);

    Mono<Void> deleteById(Integer beerId);
}

package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.domain.BeerDTO;
import guru.springframework.spring6reactive.domain.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerDTO> listAll();

    Mono<CustomerDTO> getById(Integer beerId);

    Mono<CustomerDTO> save(CustomerDTO customerDTO);

    Mono<CustomerDTO> updateCustomer(Integer beerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patchCustomer(Integer beerId, CustomerDTO customerDTO);

    Mono<Void> deleteById(Integer beerId);
}

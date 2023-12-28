package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.BeerDTO;
import guru.springframework.spring6reactive.domain.CustomerDTO;
import guru.springframework.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listBeers(){
        return customerService.listAll();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getBeerById(@PathVariable("customerId") Integer beerId){
        return customerService.getById(beerId);
    }
    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody CustomerDTO customerDTO){
        return customerService.save(customerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080/" + CUSTOMER_PATH
                                        + "/" + savedDto.getId())
                                .build().toUri())
                        .build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> updateBeer(@PathVariable("customerId") Integer customerId
            ,@Validated @RequestBody CustomerDTO customerDTO){
        return customerService.updateCustomer(customerId, customerDTO).map(updatedDto -> ResponseEntity.noContent().build());
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchBeer(@PathVariable("customerId") Integer customerId,@RequestBody CustomerDTO customerDTO){
        return customerService.patchCustomer(customerId, customerDTO).map(updatedDto -> ResponseEntity.ok().build());
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeer(@PathVariable("customerId") Integer customerId){
        return customerService.deleteById(customerId).thenReturn(ResponseEntity.noContent().build());
    }
}

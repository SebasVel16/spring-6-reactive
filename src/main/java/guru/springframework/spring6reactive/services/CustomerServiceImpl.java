package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.domain.BeerDTO;
import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.domain.CustomerDTO;
import guru.springframework.spring6reactive.mappers.CustomerMapper;
import guru.springframework.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public Flux<CustomerDTO> listAll() {
        return customerRepository.findAll().map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> getById(Integer customerId) {
        return customerRepository.findById(customerId).map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(customerDTO))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer beerId, CustomerDTO customerDTO) {
        return customerRepository.findById(beerId)
                .map(foundCustomer -> {
                    foundCustomer.setName(customerDTO.getName());
                    foundCustomer.setEmail(customerDTO.getEmail());
                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(Integer beerId, CustomerDTO customerDTO) {
        return customerRepository.findById(beerId).map(foundCustomer -> {
                    if (StringUtils.hasText(customerDTO.getName())) {
                        foundCustomer.setName(customerDTO.getName());
                    }
                    if (StringUtils.hasText(customerDTO.getEmail())) {
                        foundCustomer.setEmail(customerDTO.getEmail());
                    }
                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<Void> deleteById(Integer beerId) {
        return customerRepository.deleteById(beerId);
    }
}


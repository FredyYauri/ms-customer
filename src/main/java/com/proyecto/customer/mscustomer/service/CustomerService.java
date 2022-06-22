package com.proyecto.customer.mscustomer.service;

import com.proyecto.customer.mscustomer.entity.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> getAll();
    Mono<Customer> getByID(Integer id);
    Mono<Customer> saveCustomer(Customer customer);
    Mono<Customer> updateCutsomer(Customer customer);
    Mono<Customer> deleteCustomer(Integer id);
}

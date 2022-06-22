package com.proyecto.customer.mscustomer.controller;

import com.proyecto.customer.mscustomer.entity.Customer;
import com.proyecto.customer.mscustomer.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Flux<Customer> getCustomers (){
       return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getCustomer (@PathVariable Integer id){
        return customerService.getByID(id);
    }

    @PostMapping
    public Mono<Customer> saveCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    @PutMapping
    public Mono<Customer> updateCustomer(@RequestBody Customer customer){
        return customerService.updateCutsomer(customer);
    }

    @DeleteMapping("/{id}")
    public Mono<Customer> deleteCustomer(@PathVariable Integer id){
        return customerService.deleteCustomer(id);
    }
}

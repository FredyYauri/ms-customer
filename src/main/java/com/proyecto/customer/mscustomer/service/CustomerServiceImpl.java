package com.proyecto.customer.mscustomer.service;

import com.proyecto.customer.mscustomer.entity.Customer;
import com.proyecto.customer.mscustomer.repository.CustomerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerCrudRepository customerCrudRepository;
    @Override
    public Flux<Customer> getAll() {
        return customerCrudRepository.findAll();
    }

    @Override
    public Mono<Customer> getByID(Integer id) {
        return customerCrudRepository.findById(id);
    }

    @Override
    public Mono<Customer> saveCustomer(Customer customer){
        return customerCrudRepository.save((customer));
    }

    @Override
    public Mono<Customer> updateCutsomer(Customer customer){
        return customerCrudRepository.findById(customer.getIdCustomer())
                .flatMap(objeto -> {
                    objeto.setIdCustomer(customer.getIdCustomer());
                    objeto.setName(customer.getName());
                    objeto.setDocumentType(customer.getDocumentType());
                    objeto.setDocumentNumber(customer.getDocumentNumber());
                    objeto.setCustomerType(customer.getCustomerType());
                    return customerCrudRepository.save(objeto);
                });
    }

    @Override
    public Mono<Customer> deleteCustomer(Integer id){
        return customerCrudRepository.findById(id)
                .flatMap(encontrado->customerCrudRepository.delete(encontrado)
                        .then(Mono.just(encontrado)));
    }
}

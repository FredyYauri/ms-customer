package com.proyecto.customer.mscustomer.repository;

import com.proyecto.customer.mscustomer.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCrudRepository extends ReactiveMongoRepository<Customer, Integer> {
}

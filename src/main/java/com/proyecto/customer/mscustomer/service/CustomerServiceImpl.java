package com.proyecto.customer.mscustomer.service;

import com.proyecto.customer.mscustomer.config.CacheConfig;
import com.proyecto.customer.mscustomer.entity.Customer;
import com.proyecto.customer.mscustomer.repository.CustomerCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Cacheable cuando se consulta a la bd, @CachePut  cuando se modifica un dato de bd, @CacheEvict cuando se eliminan datos de bd. no aplica en nuevo dato
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CustomerCrudRepository customerCrudRepository;

    @Override
    public Flux<Customer> getAll() {
        return customerCrudRepository.findAll().cache();
    }

    @Override
    public Mono<Customer> getByID(Integer id) {
        String key = "customer_" + id;
        ValueOperations<String, Customer> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            Customer customer = operations.get(key);
            log.info("getByID :: customer encontrado con id: " + customer.getIdCustomer());
            return Mono.create(customerMonoSink -> customerMonoSink.success(customer));
        }
        return customerCrudRepository.findById(id).flatMap(customer -> {
            operations.set(key, customer);
            log.info("getByID :: customer guardado con key: " + key);
            return customer == null ? Mono.just(new Customer()) : Mono.just(customer);
        });
    }

    @Override
    public Mono<Customer> saveCustomer(Customer customer) {
        return customerCrudRepository.save((customer));
    }

    @Override
    public Mono<Customer> updateCutsomer(Customer customer) {
        String key = "customer_" + customer.getIdCustomer();
        ValueOperations<String, Customer> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        log.info("hasKey: " + hasKey);
        if (hasKey) {
            redisTemplate.delete(key);
            log.info("updateCutsomer :: eliminado cache antes de actualizar: " + customer.getIdCustomer());
        }

        return customerCrudRepository.findById(customer.getIdCustomer())
                .flatMap(objeto -> {
                    objeto.setIdCustomer(customer.getIdCustomer());
                    objeto.setName(customer.getName());
                    objeto.setDocumentType(customer.getDocumentType());
                    objeto.setDocumentNumber(customer.getDocumentNumber());
                    objeto.setCustomerType(customer.getCustomerType());
                    return customerCrudRepository.save(objeto).flatMap(customer1 -> {
                        operations.set(key, customer1);
                        log.info("updateCutsomer :: key registrado al actualizar: " + key);
                        return Mono.just(customer1);
                    });
                });
    }

    @Override
    public Mono<Customer> deleteCustomer(Integer id) {
        String key = "customer_" + id;
        ValueOperations<String, Customer> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        log.info("hasKey: " + hasKey);
        if (hasKey) {
            redisTemplate.delete(key);
            log.info("deleteCustomer :: eliminado cache: " + key);
        }
        return customerCrudRepository.findById(id)
                .flatMap(encontrado -> customerCrudRepository.delete(encontrado)
                        .then(Mono.just(encontrado)));
    }
}

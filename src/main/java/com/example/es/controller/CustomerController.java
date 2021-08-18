package com.example.es.controller;

import com.example.es.model.Customer;
import com.example.es.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/save")
    public int saveCustomer(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return 1;
    }

    @GetMapping("/findAll")
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/findById/{id}")
    public Customer findByFirstName(@PathVariable String id) {
        return customerRepository.findById(id).orElse(null);
    }
}

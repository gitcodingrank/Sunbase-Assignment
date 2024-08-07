package com.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.enities.Customer;
import com.customer.exception.CustomerNotFoundException;
import com.customer.exception.SomethingWentWrongException;
import com.customer.repository.CustomerRepository;

@Service
public class CustomerServiceImplementation implements CustomerService {
	
	
	@Autowired
	CustomerRepository customerRepo;

	@Override
	public Customer createCustomer(Customer customer) throws SomethingWentWrongException, CustomerNotFoundException {
		 return customerRepo.save(customer);
	}

	@Override
	public Customer updateCustomer(Customer customer) throws SomethingWentWrongException, CustomerNotFoundException {
		return customerRepo.save(customer);
	}

	@Override
	public List<Customer> getAllCustomer() throws SomethingWentWrongException, CustomerNotFoundException {
		return customerRepo.findAll();
	}

	@Override
	public Customer getCustomerById(Integer customerId) throws SomethingWentWrongException, CustomerNotFoundException {
		return customerRepo.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Invalid Customer Id"));
	}

	@Override
	public Customer deleteCustomerById(Integer customerId)
			throws SomethingWentWrongException, CustomerNotFoundException {
		Customer cust = customerRepo.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Invalid Customer Id"));
		customerRepo.deleteById(customerId);
		return cust;
	}

	@Override
	public Customer getCustomerByEmail(String email) throws SomethingWentWrongException, CustomerNotFoundException {
		return customerRepo.findByEmail(email).orElseThrow(()->new CustomerNotFoundException("Invalid Email Id"));
	}

}

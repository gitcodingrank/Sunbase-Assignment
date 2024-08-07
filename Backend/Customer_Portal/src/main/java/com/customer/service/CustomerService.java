package com.customer.service;

import java.util.List;

import com.customer.enities.Customer;
import com.customer.exception.CustomerNotFoundException;
import com.customer.exception.SomethingWentWrongException;

public interface CustomerService {
	
	Customer createCustomer(Customer customer) throws SomethingWentWrongException, CustomerNotFoundException;
	Customer updateCustomer(Customer customer) throws SomethingWentWrongException, CustomerNotFoundException;
	List<Customer> getAllCustomer() throws SomethingWentWrongException, CustomerNotFoundException;
	Customer getCustomerById(Integer customerId) throws SomethingWentWrongException, CustomerNotFoundException;
	Customer deleteCustomerById(Integer customerId) throws SomethingWentWrongException, CustomerNotFoundException;
	Customer getCustomerByEmail(String email) throws SomethingWentWrongException, CustomerNotFoundException;

}

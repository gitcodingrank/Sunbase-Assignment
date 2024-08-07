package com.customer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
		customerRepo.findById(customer.getCustomerId()).orElseThrow(()->new CustomerNotFoundException("Invalid Customer Id"));
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

	@Override
	public List<Customer> customSortByAnyOrder(String fieldOne, String directionOne, String fieldTwo, String directionTwo){
		  
		Sort sort = null;
	      if(directionOne.equalsIgnoreCase("asc")) {
	    	  sort  = Sort.by(fieldOne);
	       }
	      else {
	    	  sort = Sort.by(Sort.Direction.DESC, fieldOne);
	      }
	      
	      if(directionTwo.equalsIgnoreCase("asc")) {
	    	  sort  = Sort.by(fieldTwo);
	      }
	      else {
	    	  sort = Sort.by(Sort.Direction.DESC, fieldTwo);
	      }
	      
	      return customerRepo.findAll(sort);


     }

	@Override
	public List<Customer> getCustomerPageWise(int page, int size) {
		Pageable pg = PageRequest.of(page, size);
		Page<Customer> pageCustomer = customerRepo.findAll(pg);
		
		if(!pageCustomer.hasContent()) {
			throw new CustomerNotFoundException("No Record found for page no"+page);
		}
		
		return pageCustomer.getContent();	
	}
	
	@Override
	public List<Customer> searchCustomers(int page, int size, String field, String value) {
	        Pageable pageable = PageRequest.of(page, size);
	        List<Customer> allCustomers = customerRepo.findAll(pageable).getContent();

	        return allCustomers.stream()
	                .filter(customer -> {
	                    switch (field.toLowerCase()) {
	                        case "firstname":
	                            return customer.getFirstName() != null && customer.getFirstName().toLowerCase().contains(value.toLowerCase());
	                        case "lastname":
	                            return customer.getLastName() != null && customer.getLastName().toLowerCase().contains(value.toLowerCase());
	                        case "email":
	                            return customer.getEmail() != null && customer.getEmail().toLowerCase().contains(value.toLowerCase());
	                        case "phonenumber":
	                            return customer.getPhone() != null && customer.getPhone().toLowerCase().contains(value.toLowerCase());
	                        case "city":
	                            return customer.getCity() != null && customer.getCity().toLowerCase().contains(value.toLowerCase());
	                        default:
	                            return false;
	                    }
	                })
	                .collect(Collectors.toList());
	    }
}

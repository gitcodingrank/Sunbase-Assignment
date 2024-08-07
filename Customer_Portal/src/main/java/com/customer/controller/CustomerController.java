package com.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.enities.Customer;
import com.customer.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/register")
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer){
		
		if(customer.getPassword()!=null) {
			customer.setPassword(passwordEncoder.encode(customer.getPassword()));
	      }
		customer.setRole("ROLE_"+customer.getRole().toUpperCase());
		//customer.setRole(customer.getRole().toUpperCase());
		Customer registeredCustomer = customerService.createCustomer(customer);
		
	 return new ResponseEntity<Customer>(registeredCustomer, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Customer>> getAllCustomers(){
		
	 return new ResponseEntity<List<Customer>>(customerService.getAllCustomer(), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId ){
		
	 return new ResponseEntity<Customer>(customerService.getCustomerById(customerId), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/customorder/{fieldOne}/{directionOne}/{fieldTwo}/{directionTwo}")
	public ResponseEntity<List<Customer>> customSortByAnyOrder(@PathVariable String fieldOne, @PathVariable String directionOne, @PathVariable String fieldTwo, @PathVariable String directionTwo){
		return new ResponseEntity<List<Customer>>(customerService.customSortByAnyOrder(fieldOne, directionOne, fieldTwo, directionTwo), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{page}/{size}")
	public  ResponseEntity<List<Customer>> getCustomerPageWise( @PathVariable int page, @PathVariable int size) {
		return new ResponseEntity<List<Customer>>(customerService.getCustomerPageWise(page, size), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/signin")
	public ResponseEntity<String> userLogin(Authentication auth){
		 log.info("------------------------------------------------------------------");
	     log.info(auth.getName());
		 Customer customer = customerService.getCustomerByEmail(auth.getName());
		 log.info("------------------------------------------------------------------");
		 log.info(customer.getFirstName());	
		return new ResponseEntity<String>(customer.getFirstName()+" has logged in successfully.", HttpStatus.OK);
	}
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Customer> deleteCustomerById(@PathVariable Integer customerId ){
		
	 return new ResponseEntity<Customer>(customerService.deleteCustomerById	(customerId), HttpStatus.ACCEPTED);
	}
	
    @PutMapping("/update")
	public ResponseEntity<Customer>  updateCustomer(@RequestBody Customer customer) {
    	return new ResponseEntity<Customer>(customerService.updateCustomer(customer), HttpStatus.ACCEPTED);
	}

	@GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Perform any additional logout logic here, if needed
        
        // Invalidate the session
        request.getSession().invalidate();
        
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
	
	@GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomers(
            @RequestParam String field,
            @RequestParam String value,
            @RequestParam int page,
            @RequestParam int size) {
        return new ResponseEntity<List<Customer>>(customerService.searchCustomers( page, size, field, value), HttpStatus.ACCEPTED);
    }

}

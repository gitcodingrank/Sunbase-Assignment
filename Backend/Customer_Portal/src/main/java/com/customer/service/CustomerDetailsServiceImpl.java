package com.customer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.customer.enities.Customer;
import com.customer.repository.CustomerRepository;



@Service
public class CustomerDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private CustomerRepository customerRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Optional<Customer> opt= customerRepo.findByEmail(username);

		if(opt.isPresent()) {
			
			Customer customer= opt.get();
			
			//Empty Authorities
			List<GrantedAuthority> authorities= new ArrayList<>();
			SimpleGrantedAuthority sga = new SimpleGrantedAuthority(customer.getRole());
			authorities.add(sga);
			
			return new User(customer.getEmail(), customer.getPassword(), authorities);
			
			//return new CustomerUserDetails(customer);
			
			
		}else
			throw new BadCredentialsException("Customer Details not found with this username: "+username);
	}

}
package com.customer.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyErrorDetail {
   
	private LocalDateTime timestamp;
	private String message;
	private String url;
}

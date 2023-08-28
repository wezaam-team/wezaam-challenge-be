package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodService {

	@Autowired
	PaymentMethodRepository paymentMethodRepository;

	public PaymentMethod findById(Long id){
		return paymentMethodRepository.findById(id).orElseThrow();
	}
}

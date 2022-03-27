package com.wezaam.withdrawal.rest;

import com.wezaam.withdrawal.exception.PaymentMethodNotFoundException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.model.User;
import com.wezaam.withdrawal.service.PaymentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api
@RestController
public class PaymentMethodController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/find-payment-method-by-name/{name}")
    public ResponseEntity<PaymentMethod> findByUserName(@PathVariable String name) throws Exception {
        PaymentMethod payment = paymentService.findByName(name);
        return new ResponseEntity<PaymentMethod>(payment, HttpStatus.OK);
    }

    @GetMapping("/find-payment-method-by-id/{id}")
    public ResponseEntity<PaymentMethod> findByUserName(@PathVariable Long id) throws Exception {
        PaymentMethod payment = paymentService.findById(id);
        return new ResponseEntity<PaymentMethod>(payment, HttpStatus.OK);
    }

    @PostMapping("/add-payment-methods")
    public List<PaymentMethod> addPaymentMethods(@RequestBody List<PaymentMethod> paymentMehods) {
        return paymentService.addPaymentMethods(paymentMehods);
    }

    @PostMapping("/add-payment-method")
    public PaymentMethod addPaymentMethod(@RequestBody PaymentMethod paymentMehod) {
        return paymentService.addPaymentMethod(paymentMehod);
    }

    @DeleteMapping("delete-payment-method")
    public void deletePaymentMethod(@RequestBody PaymentMethod paymentMethod) throws Exception {
        paymentService.deletePaymentMethod(paymentMethod);
    }

    @DeleteMapping("delete-payment-methods")
    public void deletePaymentMethods(@RequestBody List<PaymentMethod> paymentMethods) {
        paymentService.deletePaymentMethods(paymentMethods);
    }


}

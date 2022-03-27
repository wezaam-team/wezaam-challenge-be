package com.wezaam.withdrawal.service;

import com.wezaam.withdrawal.exception.PaymentMethodNotFoundException;
import com.wezaam.withdrawal.model.PaymentMethod;
import com.wezaam.withdrawal.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;


    @Override
    public PaymentMethod findByName(String name) throws PaymentMethodNotFoundException {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findByName(name);
        if (!paymentMethod.isPresent()) throw new PaymentMethodNotFoundException("Payment method not found in the system");
        return paymentMethod.get();
    }

    @Override
    public PaymentMethod findById(Long id) throws Exception {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (!paymentMethod.isPresent()) throw new PaymentMethodNotFoundException("Payment method not found in the system");
        return paymentMethod.get();
    }

    @Override
    public List<PaymentMethod> addPaymentMethods(List<PaymentMethod> paymentMethods) {
        return paymentMethodRepository.saveAll(paymentMethods);
    }

    @Override
    public PaymentMethod addPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void deletePaymentMethod(PaymentMethod paymentMethod) {
        try {
                paymentMethodRepository.delete(paymentMethod);
        }catch(Exception e) {
            throw new RuntimeException(String.format("The payment %s to delete not exist", paymentMethod.getName()));
        }

    }

    @Override
    public void deletePaymentMethods(List<PaymentMethod> list) {

        list.forEach( p -> deletePaymentMethod(p));

        //paymentMethodRepository.deleteAll(list);
    }
}

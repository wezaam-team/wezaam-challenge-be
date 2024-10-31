package com.wezaam.withdrawal.infrastructure.dataaccess.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.infrastructure.dataaccess.entity.PaymentMethod;
import com.wezaam.withdrawal.infrastructure.dataaccess.entity.User;

@Component
public class UserDataAccessMapper {

    public List<com.wezaam.withdrawal.domain.entity.User> mapUserListAsUserResponseList(
            List<User> userList) {
        return userList.stream().map(this::mapUserEntityAsUser).toList();
    }

    public com.wezaam.withdrawal.domain.entity.User mapUserEntityAsUser(User user) {

        return com.wezaam.withdrawal.domain.entity.User.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .paymentMethods(mapAsPaymentMethodList(user.getPaymentMethods()))
                .maxWithdrawalAmount(user.getMaxWithdrawalAmount())
                .build();
    }

    List<com.wezaam.withdrawal.domain.entity.PaymentMethod> mapAsPaymentMethodList(
            List<PaymentMethod> paymentMethods) {
        return paymentMethods.stream().map(this::mapPaymentMethodAsPaymentMethod).toList();
    }

    com.wezaam.withdrawal.domain.entity.PaymentMethod mapPaymentMethodAsPaymentMethod(
            PaymentMethod paymentMethod) {
        return com.wezaam.withdrawal.domain.entity.PaymentMethod.builder()
                .id(paymentMethod.getId())
                .name(paymentMethod.getName())
                .build();
    }
}

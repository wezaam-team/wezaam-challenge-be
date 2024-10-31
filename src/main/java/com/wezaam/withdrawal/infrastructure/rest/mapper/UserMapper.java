package com.wezaam.withdrawal.infrastructure.rest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wezaam.withdrawal.domain.entity.PaymentMethod;
import com.wezaam.withdrawal.domain.entity.User;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.PaymentMethodResponse;
import com.wezaam.withdrawal.infrastructure.rest.dto.response.UserResponse;

@Component
public class UserMapper {

    public List<UserResponse> mapUserListAsUserResponseList(List<User> userList) {
        return userList.stream().map(this::mapUserAsUserResponse).toList();
    }

    public UserResponse mapUserAsUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .paymentMethods(mapAsPaymentMethodResponseList(user.getPaymentMethods()))
                .maxWithdrawalAmount(user.getMaxWithdrawalAmount())
                .build();
    }

    List<PaymentMethodResponse> mapAsPaymentMethodResponseList(List<PaymentMethod> paymentMethods) {
        return paymentMethods.stream().map(this::mapPaymentMethodAsPaymentMethodResponse).toList();
    }

    PaymentMethodResponse mapPaymentMethodAsPaymentMethodResponse(PaymentMethod paymentMethod) {
        return PaymentMethodResponse.builder()
                .id(paymentMethod.getId())
                .name(paymentMethod.getName())
                .build();
    }
}

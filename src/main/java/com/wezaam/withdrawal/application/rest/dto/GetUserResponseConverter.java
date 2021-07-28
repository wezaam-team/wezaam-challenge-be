package com.wezaam.withdrawal.application.rest.dto;

import com.wezaam.withdrawal.domain.User;

import java.util.function.Function;
import java.util.stream.Collectors;

public class GetUserResponseConverter implements Function<User, GetUserResponse> {

    private GetUserResponseConverter() {
        super();
    }

    public static GetUserResponseConverter aGetUsersResponseConverter() {
        return new GetUserResponseConverter();
    }

    @Override
    public GetUserResponse apply(User user) {
        return GetUserResponseBuilder
                .aGetUsersResponseBuilder()
                .withId(user.getId())
                .withName(user.getName())
                .withPaymentIds(
                        user.getPaymentMethods().
                                stream()
                                .map(paymentMethod -> paymentMethod.getId())
                                .collect(Collectors.toList())
                )
                .build();
    }
}

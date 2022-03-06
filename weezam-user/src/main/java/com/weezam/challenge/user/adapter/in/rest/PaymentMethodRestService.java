package com.weezam.challenge.user.adapter.in.rest;

import com.weezam.challenge.user.adapter.in.rest.dto.PaymentMethodDto;
import com.weezam.challenge.user.adapter.in.rest.dto.UserDto;
import com.weezam.challenge.user.adapter.in.rest.mapper.PaymentMethodMapper;
import com.weezam.challenge.user.adapter.in.rest.mapper.UserMapper;
import com.weezam.challenge.user.domain.PaymentMethodAggregate;
import com.weezam.challenge.user.domain.UserAggregate;
import com.weezam.challenge.user.domain.exception.InvalidCriteriaException;
import com.weezam.challenge.user.domain.exception.PaymentMethodNotFoundException;
import com.weezam.challenge.user.domain.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/")
@Slf4j
@AllArgsConstructor
public class PaymentMethodRestService {

    private final PaymentMethodAggregate aggregate;

    private final PaymentMethodMapper mapper;

    @GetMapping(path = "/payments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PaymentMethodDto> findOne(final @PathVariable(value = "id") Long id) {
        try {
            PaymentMethodDto res = mapper.toDto(aggregate.findOne(id));
            return new ResponseEntity<PaymentMethodDto>(res, HttpStatus.OK);
        } catch (PaymentMethodNotFoundException ex) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentMethod Not Found", ex);
        } catch (InvalidCriteriaException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentMethod Not Found", ex);
        }
    }
    
}

package com.weezam.challenge.withdrawal.adapter.in.rest;

import com.weezam.challenge.withdrawal.adapter.in.rest.dto.NewWithdrawalCommandDto;
import com.weezam.challenge.withdrawal.adapter.in.rest.dto.WithdrawalDto;
import com.weezam.challenge.withdrawal.adapter.in.rest.mapper.NewWithdrawalMapper;
import com.weezam.challenge.withdrawal.adapter.in.rest.mapper.WithdrawalMapper;
import com.weezam.challenge.withdrawal.domain.WithdrawalAggregate;
import com.weezam.challenge.withdrawal.domain.exception.WithdrawalApplicationException;
import com.weezam.challenge.withdrawal.domain.exception.WithdrawalNotFoundException;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@Slf4j
@AllArgsConstructor
public class WithdrawalRestService {

    private final WithdrawalAggregate withdrawalAggregate;

    private final NewWithdrawalMapper newWithdrawalMapper;
    private final WithdrawalMapper withdrawalMapper;

    @GetMapping(path = "/withdrawals/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<WithdrawalDto> findOne(final @PathVariable(value = "id") Long id) {
        try {
            log.info("Request to get withdrawal with ID {}", id);
            WithdrawalDto res = withdrawalMapper.toDto(withdrawalAggregate.findOne(id));
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (WithdrawalNotFoundException ex) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Withdrawal Not Found", ex);
        } catch (WithdrawalApplicationException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Withdrawal Not Found", ex);
        }
    }

    @GetMapping(path = "/withdrawals", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WithdrawalDto>> findAll() {
        log.info("Request to get all withdrawal ");
        List<WithdrawalDto> res = withdrawalAggregate.findAll().stream().map(withdrawalMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(path = "/withdrawals", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<WithdrawalDto> create(@RequestBody final NewWithdrawalCommandDto body) {
        try {
            log.info("Request to create withdrawal {}", body);
            Withdrawal model = newWithdrawalMapper.toDomain(body);
            model = withdrawalAggregate.create(model);
            return new ResponseEntity<>(withdrawalMapper.toDto(model), HttpStatus.CREATED);
        } catch (WithdrawalApplicationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdrawal bad request", ex);
        }

    }
    
}

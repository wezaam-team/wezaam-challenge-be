package com.weezam.challenge.withdrawal.adapter.out.persistence;

import com.weezam.challenge.withdrawal.adapter.out.persistence.entity.WithdrawalEntity;
import com.weezam.challenge.withdrawal.adapter.out.persistence.mapper.WithdrawalEntityMapper;
import com.weezam.challenge.withdrawal.domain.model.Withdrawal;
import com.weezam.challenge.withdrawal.domain.repository.WithdrawalRepository;
import lombok.AllArgsConstructor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
public class DbWithdrawalRepository implements WithdrawalRepository {

    private final WithdrawalRepositoryJpa jpaWithdrawalRepository;

    private final WithdrawalEntityMapper withdrawalEntityMapper;

    @Override
    public List<Withdrawal> findAll() {
        List<WithdrawalEntity> res = jpaWithdrawalRepository.findAll();
        return withdrawalEntityMapper.toDomain(res);
    }

    @Override
    public Optional<Withdrawal> findOne(Long id) {
        WithdrawalEntity userEntity = jpaWithdrawalRepository.getById(id);
        return Optional.ofNullable(withdrawalEntityMapper.toDomain(userEntity));
    }

    @Override
    public Withdrawal save(Withdrawal withdrawal) {
        WithdrawalEntity entity = jpaWithdrawalRepository.save(withdrawalEntityMapper.toEntity(withdrawal));
        return withdrawalEntityMapper.toDomain(entity);
    }
}

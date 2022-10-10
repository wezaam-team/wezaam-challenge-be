package com.wezaam.withdrawal.port.adapter.persistence;

import com.wezaam.withdrawal.domain.model.Withdrawal;
import com.wezaam.withdrawal.domain.model.WithdrawalRepository;
import com.wezaam.withdrawal.domain.model.WithdrawalStatus;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class H2JpaWithdrawalRepository implements WithdrawalRepository {

    private final SpringJpaWithdrawalRepository springJpaWithdrawalRepository;
    private final EntityManager entityManager;

    public H2JpaWithdrawalRepository(
            SpringJpaWithdrawalRepository springJpaWithdrawalRepository, EntityManager entityManager) {

        if (springJpaWithdrawalRepository == null) {
            throw new IllegalArgumentException("The springJpaWithdrawalRepository should not be null.");
        }

        if (entityManager == null) {
            throw new IllegalArgumentException("The entityManager should not be null.");
        }

        this.springJpaWithdrawalRepository = springJpaWithdrawalRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Long getNextId() {
        return ((BigInteger) entityManager.createNativeQuery("SELECT NEXT VALUE FOR withdrawal_seq").getSingleResult())
                .longValue();
    }

    @Override
    public void save(Withdrawal withdrawal) {
        this.springJpaWithdrawalRepository.save(withdrawal);
    }

    @Override
    public Optional<Withdrawal> getWithdrawalOfId(long id) {
        return this.springJpaWithdrawalRepository.findById(id);
    }

    @Override
    public List<Withdrawal> getAll() {
        return this.springJpaWithdrawalRepository.findAll();
    }

    @Override
    public List<Withdrawal> getScheduledWithdrawalsReadyForProcessing() {
        return entityManager.createQuery(
                "select withdrawal from withdrawals as withdrawal where withdrawal.status = ?1"
                        + " and withdrawal.executeAt is not null and withdrawal.executeAt <= ?2",
                Withdrawal.class)
                .setParameter(1, WithdrawalStatus.PENDING)
                .setParameter(2, Instant.now())
                .getResultList();
    }
}

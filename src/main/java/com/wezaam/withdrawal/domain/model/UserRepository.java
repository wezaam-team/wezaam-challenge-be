package com.wezaam.withdrawal.domain.model;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserOfId(long id);

    List<User> getAll();
}

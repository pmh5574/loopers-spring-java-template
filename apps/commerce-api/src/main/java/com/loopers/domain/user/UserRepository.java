package com.loopers.domain.user;

public interface UserRepository {
    UserModel save(UserModel model);

    boolean existsByUserId(String userId);
}

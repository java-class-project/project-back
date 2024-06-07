package com.example.oopclass.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByStudentNumber(String studentNumber);
    Optional<User> findByUserUuid(UUID userUuid);

//    Optional<User> findById(UUID userUuid);

}

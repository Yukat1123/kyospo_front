package com.example.kyoto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // ユーザー名で検索するメソッドを追加
    Optional<User> findByUsername(String username);
}
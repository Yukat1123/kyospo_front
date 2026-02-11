package com.example.kyoto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    // メソッド名を Containing に変更
    List<Spot> findByTagIdContaining(String tagId);
}
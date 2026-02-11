package com.example.kyoto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // 特定のユーザーのお気に入り一覧を取得する
    List<Favorite> findByUserId(Long userId);
    
    // すでにお気に入り登録済みか確認するために使用
    boolean existsByUserIdAndSpotId(Long userId, Long spotId);
    
    // 特定のユーザーの特定のスポットお気に入りを削除する
    @Transactional
    void deleteByUserIdAndSpotId(Long userId, Long spotId);
}
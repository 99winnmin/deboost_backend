package com.samnamja.deboost.api.entity.riot.UserHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    Optional<UserHistory> findByHistoryGamerName(String historyGamerName);
}

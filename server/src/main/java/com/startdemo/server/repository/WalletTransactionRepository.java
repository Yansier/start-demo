package com.startdemo.server.repository;

import com.startdemo.server.domain.TransactionType;
import com.startdemo.server.domain.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByTransactionId(String transactionId);

    List<WalletTransaction> findByUidOrderByCreatedAtDesc(String uid);

    boolean existsByRelatedTransactionIdAndType(String relatedTransactionId, TransactionType type);

    void deleteByUid(String uid);
}

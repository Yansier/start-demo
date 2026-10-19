package com.startdemo.server.repository;

import com.startdemo.server.domain.TransactionType;
import com.startdemo.server.domain.WalletTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByTransactionId(String transactionId);

    List<WalletTransaction> findByUidOrderByCreatedAtDesc(String uid);

    List<WalletTransaction> findTop20ByUidOrderByCreatedAtDesc(String uid);

    @Query("""
            select t from WalletTransaction t
            where (:uid is null or t.uid = :uid)
              and (:type is null or t.type = :type)
            order by t.createdAt desc
            """)
    List<WalletTransaction> search(
            @Param("uid") String uid,
            @Param("type") TransactionType type,
            Pageable pageable
    );

    @Query("""
            select count(t) from WalletTransaction t
            where t.createdAt >= :from and t.createdAt < :to
            """)
    long countCreatedBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
            select count(t) from WalletTransaction t
            where t.type = :type and t.createdAt >= :from and t.createdAt < :to
            """)
    long countByTypeCreatedBetween(
            @Param("type") TransactionType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    @Query("""
            select coalesce(sum(t.amount), 0) from WalletTransaction t
            where t.type = :type and t.createdAt >= :from and t.createdAt < :to
            """)
    BigDecimal sumAmountByTypeCreatedBetween(
            @Param("type") TransactionType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    boolean existsByRelatedTransactionIdAndType(String relatedTransactionId, TransactionType type);

    void deleteByUid(String uid);
}

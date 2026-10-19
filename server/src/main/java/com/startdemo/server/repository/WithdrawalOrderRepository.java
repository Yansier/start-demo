package com.startdemo.server.repository;

import com.startdemo.server.domain.PaymentStatus;
import com.startdemo.server.domain.WithdrawalOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WithdrawalOrderRepository extends JpaRepository<WithdrawalOrder, Long> {

    Optional<WithdrawalOrder> findByOrderNo(String orderNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from WithdrawalOrder w where w.orderNo = :orderNo")
    Optional<WithdrawalOrder> findByOrderNoForUpdate(@Param("orderNo") String orderNo);

    List<WithdrawalOrder> findByUidOrderByCreatedAtDesc(String uid);

    long countByStatus(PaymentStatus status);

    @Query("""
            select w from WithdrawalOrder w
            where (:uid is null or w.uid = :uid)
              and (:status is null or w.status = :status)
            order by w.createdAt desc
            """)
    List<WithdrawalOrder> search(
            @Param("uid") String uid,
            @Param("status") PaymentStatus status,
            Pageable pageable
    );
}

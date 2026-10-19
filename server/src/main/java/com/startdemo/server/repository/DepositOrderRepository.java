package com.startdemo.server.repository;

import com.startdemo.server.domain.DepositOrder;
import com.startdemo.server.domain.PaymentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepositOrderRepository extends JpaRepository<DepositOrder, Long> {

    Optional<DepositOrder> findByOrderNo(String orderNo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from DepositOrder d where d.orderNo = :orderNo")
    Optional<DepositOrder> findByOrderNoForUpdate(@Param("orderNo") String orderNo);

    List<DepositOrder> findByUidOrderByCreatedAtDesc(String uid);

    long countByStatus(PaymentStatus status);

    @Query("""
            select d from DepositOrder d
            where (:uid is null or d.uid = :uid)
              and (:status is null or d.status = :status)
            order by d.createdAt desc
            """)
    List<DepositOrder> search(
            @Param("uid") String uid,
            @Param("status") PaymentStatus status,
            Pageable pageable
    );
}

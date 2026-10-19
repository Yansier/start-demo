package com.startdemo.server.repository;

import com.startdemo.server.domain.DemoWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DemoWalletRepository extends JpaRepository<DemoWallet, Long> {

    Optional<DemoWallet> findByUid(String uid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from DemoWallet w where w.uid = :uid")
    Optional<DemoWallet> findByUidForUpdate(@Param("uid") String uid);
}

package com.bookseva.dispute.repository;

import com.bookseva.dispute.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {
    @Query("SELECT d FROM Dispute d WHERE d.order.buyer.email = :email OR d.order.seller.email = :email ORDER BY d.createdAt DESC")
    List<Dispute> findByInvolvedUserEmail(@Param("email") String email);
}

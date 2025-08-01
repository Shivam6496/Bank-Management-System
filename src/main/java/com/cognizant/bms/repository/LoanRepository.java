package com.cognizant.bms.repository;

import com.cognizant.bms.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByCustomerUsername(String username);

}
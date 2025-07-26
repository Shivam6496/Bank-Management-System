package com.cognizant.bms.service;

import com.cognizant.bms.service.ApiResponse;
import com.cognizant.bms.entity.Customer;
import com.cognizant.bms.entity.Loan;
import com.cognizant.bms.entity.User;
import com.cognizant.bms.repository.CustomerRepository;
import com.cognizant.bms.repository.LoanRepository;
import com.cognizant.bms.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private HttpSession session;

    public ResponseEntity<ApiResponse> registerCustomer(Customer customer) {
        if (userRepository.findByUsername(customer.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username already exists"));
        }

        customerRepository.save(customer);

        User user = new User();
        user.setUsername(customer.getUsername());
        user.setPassword(passwordEncoder.encode(customer.getPassword()));
        user.setRole("USER");
        user.setEnabled(true);
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Customer registered successfully"));
    }

    public ResponseEntity<ApiResponse> updateCustomerDetails(Customer customer) {
        customerRepository.save(customer);
        return ResponseEntity.ok(new ApiResponse(true, "Customer details updated successfully"));
    }

    public ResponseEntity<ApiResponse> applyLoan(Loan loan) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "User not logged in"));
        }

        Customer customer = customerRepository.findByUsername(username);
        loan.setCustomer(customer);
        loanRepository.save(loan);

        return ResponseEntity.ok(new ApiResponse(true, "Loan applied successfully"));
    }

    public List<Loan> getLoansByUsername(String username) {
        return loanRepository.findAllByCustomerUsername(username);
    }
}

package com.cognizant.bms.controller;

import com.cognizant.bms.entity.Customer;
import com.cognizant.bms.entity.Loan;
import com.cognizant.bms.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.hibernate.annotations.ConcreteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalTime;

@Controller
public class ViewController {
    @Autowired
    private HttpSession session;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // retrieves the logged-in username

        String greeting = getGreeting();
        model.addAttribute("username", username);
        model.addAttribute("greeting", greeting);
        return "home";
    }

    private String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) return "Good Morning";
        else if (hour >= 12 && hour < 17) return "Good Afternoon";
        else if (hour >= 17 && hour < 21) return "Good Evening";
        else return "Good Night";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }


    @GetMapping("/customer/updateCustomerDetails")
    public String showUpdateCustomerDetailsPage(Model model) {
        Customer customer = customerRepository.findByUsername((String) session.getAttribute("username"));
        model.addAttribute("customer", customer);
        return "updatedetails";
    }

    // add endpoint for apply loan page
    @GetMapping("/customer/applyLoan")
    public String applyLoan(Model model) {
        model.addAttribute("loan", new Loan());
        return "applyloan";
    }

}

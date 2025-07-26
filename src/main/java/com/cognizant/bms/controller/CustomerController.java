package com.cognizant.bms.controller;

/*
 * This controller handles customer-related operations.
 */

import com.cognizant.bms.entity.Customer;
import com.cognizant.bms.entity.Loan;
import com.cognizant.bms.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private HttpSession session;

    @PostMapping("/register")
    public String registerCustomer(@ModelAttribute("customer") Customer customer, BindingResult result, RedirectAttributes redirectAttributes){

        // Logic to register a customer
        if (result.hasErrors()) {
            return "register";
        }

        System.out.println(customer.toString());
        customerService.registerCustomer(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Customer register successfully!");
        return "redirect:/login";
    }

    @PostMapping(path = "/updateCustomerDetails")
    public String updateCustomerDetails(@ModelAttribute("customer") Customer customer, BindingResult result, RedirectAttributes redirectAttributes) {

        // Validate the input
        if (result.hasErrors()) {
            return "updateCustomer";
        }

        // Logic to update customer details
        customerService.updateCustomerDetails(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Customer details updated successfully!");
        return "redirect:/home";
    }

    @PostMapping("/applyLoan")
    public String applyLoan(@ModelAttribute("loan") Loan loan, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "applyloan";
        }

        customerService.applyLoan(loan);
        redirectAttributes.addFlashAttribute("successMessage", "Loan application submitted successfully!");
        return "redirect:/home";
    }

    @GetMapping("/viewLoans")
    public String viewLoans(Model model) {
        String username = (String) session.getAttribute("username");
        List<Loan> loans = customerService.getLoansByUsername(username);
        model.addAttribute("loans", loans);
        return "viewLoans";
    }
}

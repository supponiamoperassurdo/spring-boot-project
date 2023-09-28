package com.scibetta.controller;

import com.scibetta.repository.CreditCardDatabase;
import com.scibetta.repository.TransactionDatabase;
import com.scibetta.repository.UserDatabase;
import com.scibetta.model.CreditCard;
import com.scibetta.model.CreditCardTransactionUserView;
import com.scibetta.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class UserController {

    private final CreditCardDatabase creditCardDatabase;
    private final TransactionDatabase transactionDatabase;
    private final UserDatabase userDatabase;

    @Autowired
    public UserController(CreditCardDatabase creditCardDatabase, TransactionDatabase transactionDatabase, UserDatabase userDatabase) {
        this.creditCardDatabase = creditCardDatabase;
        this.transactionDatabase = transactionDatabase;
        this.userDatabase = userDatabase;
    }

    @GetMapping("/balance/check")
    public String balanceCheck(Model model, @RequestParam int number) {
        try {
            System.out.println("(UserController) Numero di carta: " + number);
            CreditCard creditCard = creditCardDatabase.selectByNumber(number).get();
            if (creditCard.getStatus() == 0) throw new NoSuchElementException(); // se la carta è disabilitata, lancia un eccezione
            model.addAttribute("balance", creditCard.getBalance() + " €");
        } catch (NoSuchElementException e) {
            System.out.println("(UserController) Errore: carta non presente nel database.");
            model.addAttribute("balance", "Inserire una carta valida");
        }
        return "balance";
    }

    @GetMapping("/profile/user-transactions")
    public String profileUserTransactions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDatabase.selectByUsername(authentication.getName()).get();
        List<CreditCardTransactionUserView> creditCardTransactionUserView = transactionDatabase.selectAllJoinUserForUser(authentication);
        model.addAttribute("creditCardTransactionUserView", creditCardTransactionUserView);
        model.addAttribute("fullname", user.getFirstname() + " " + user.getLastname());
        return "user-transactions";
    }

}
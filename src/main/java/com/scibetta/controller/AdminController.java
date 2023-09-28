package com.scibetta.controller;

import com.scibetta.repository.CreditCardDatabase;
import com.scibetta.repository.UserDatabase;
import com.scibetta.model.CreditCard;
import com.scibetta.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Controller
public class AdminController {

    private final CreditCardDatabase creditCardDatabase;
    private final UserDatabase userDatabase;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(CreditCardDatabase creditCardDatabase, UserDatabase userDatabase, PasswordEncoder passwordEncoder) {
        this.creditCardDatabase = creditCardDatabase;
        this.userDatabase = userDatabase;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/profile/admin/add-creditcard")
    String profileAdminAddCreditCard(@RequestParam int balance) {
        System.out.println("(AdminController) Bilancio settato a: " + balance);
        Random random = new Random();
        int number;
        while (true) {
            int possibleCreditCardNumber = (random.nextInt(100000) + 10000) % 10000;
            if (creditCardDatabase.selectByNumber(possibleCreditCardNumber).isEmpty()) { // possibleCreditCardNumber è un candidato come numero di carta
                number = possibleCreditCardNumber;
                break;
            }
        }
        creditCardDatabase.insertWithoutOwner(new CreditCard(number, balance, 1));
        System.out.println("(AdminController) Numero di carta settato a: " + number);
        return "redirect:/profile/admin?" + number;
    }

    @PostMapping("/profile/admin/add-seller")
    String profileAdminAddCreditCard(@ModelAttribute("user") User user) {
        Optional<User> possibleSeller = userDatabase.selectByUsername(user.getUsername());
        if (possibleSeller.isPresent()) {
            System.out.println("(AdminController) Indirizzo e-mail già utilizzato.");
            return "redirect:/profile/admin?adderror";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("seller");
        userDatabase.insert(user);
        System.out.println("(AdminController) " + user);
        return "redirect:/profile/admin?success?";
    }

    @PostMapping("/profile/admin/remove-seller")
    String profileAdminAddCreditCard(@RequestParam String delusername) {
        User possibleSeller;
        try {
            possibleSeller = userDatabase.selectByUsername(delusername).get();
            if (possibleSeller.getRole().equals("seller")) {
                userDatabase.deleteById(possibleSeller.getId());
                return "redirect:/profile/admin?success";
            }
        } catch (NoSuchElementException e) {
            System.out.println("(AdminController) Errore: e-mail non presente nel database.");
        }
        return "redirect:/profile/admin?removeerror";
    }

    @GetMapping("/profile/admin/manage-creditcards")
    String profileAdminMangeCreditCards(Model model) {
        List<CreditCard> creditCardList = creditCardDatabase.selectAll();
        model.addAttribute("creditCardList", creditCardList);
        return "manage-creditcards";
    }

    @PostMapping("/profile/admin/manage-creditcards/manage-status")
    String profileAdminMangeCreditCardsManageStatus(@RequestParam String number) {
        CreditCard creditCard;
        try {
            creditCard = creditCardDatabase.selectByNumber(Integer.parseInt(number)).get();
            int oldStatus = creditCard.getStatus();
            if (creditCard.getStatus() == 0) creditCard.setStatus(1); else creditCard.setStatus(0);
            System.out.println("(AdminController) Stato della carta " + number + " cambiato da " + oldStatus + " a " + creditCard.getStatus());
            creditCardDatabase.updateById(creditCard.getId(), new CreditCard(creditCard.getId(), creditCard.getNumber(), creditCard.getBalance(), creditCard.getOwner(), creditCard.getStatus()));
        } catch (NoSuchElementException e) {
            System.out.println("(AdminController) Errore: carta non presente nel database.");
        }
        return "redirect:/profile/admin/manage-creditcards";
    }

}
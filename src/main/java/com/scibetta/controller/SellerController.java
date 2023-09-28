package com.scibetta.controller;

import com.scibetta.repository.CreditCardDatabase;
import com.scibetta.repository.TransactionDatabase;
import com.scibetta.repository.UserDatabase;
import com.scibetta.model.CreditCard;
import com.scibetta.model.CreditCardTransactionUserView;
import com.scibetta.model.Transaction;
import com.scibetta.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class SellerController {

    private final CreditCardDatabase creditCardDatabase;
    private final UserDatabase userDatabase;
    private final TransactionDatabase transactionDatabase;

    @Autowired
    public SellerController(CreditCardDatabase creditCardDatabase, UserDatabase userDatabase, TransactionDatabase transactionDatabase) {
        this.creditCardDatabase = creditCardDatabase;
        this.userDatabase = userDatabase;
        this.transactionDatabase = transactionDatabase;
    }

    @GetMapping("/profile/seller")
    String profileSeller() {
        return "seller";
    }

    @PostMapping("/profile/seller/generate-report")
    String profileSellerGenerateReport(Authentication authentication, @RequestParam String number, String balance, String checkbox, String description) {

        boolean ischarge = true;
        CreditCard creditCard;
        int operation;

        if (checkbox == null) ischarge = false;
        if (!ischarge) operation = Integer.parseInt(balance);
        else operation = - Integer.parseInt(balance);

        try {

            /* ACCREDITO/ADDEBITO */

            creditCard = creditCardDatabase.selectByNumber(Integer.parseInt(number)).get();
            if (creditCard.getStatus() == 0) throw new NoSuchElementException(); // se la carta è disabilitata, lancia un eccezione
            int creditCardBalance = creditCard.getBalance() + operation;
            CreditCard updatedCreditCard = new CreditCard(creditCard.getId(), creditCard.getNumber(), creditCardBalance, creditCard.getOwner(), creditCard.getStatus());
            creditCardDatabase.updateById(creditCard.getId(), updatedCreditCard);

            /* GENERAZIONE DEI REPORT */

            int userid = updatedCreditCard.getOwner();
            if (userid < 1) throw new NoSuchElementException(); // se l'utente ha id 0, c'è stato un errore di assegnazione nel server
            String sellerUsername = authentication.getName();
            int sellerid = userDatabase.selectByUsername(sellerUsername).get().getId();
            Transaction transaction = new Transaction(description, userid, sellerid, new Timestamp(System.currentTimeMillis()), operation, creditCard.getNumber());
            transactionDatabase.insert(transaction);
            System.out.println("(SellerController) " + transaction);
            return "redirect:/profile/seller?success";


        } catch (NoSuchElementException e) {

            System.out.println("(SellerController) Errore: carta non presente nel database.");
            return "redirect:/profile/seller?error";

        }

    }

    @PostMapping("/profile/seller/assign-creditcard")
    String profileSellerGenerateReport(@RequestParam String ccnumber, String username) {

        User user;
        CreditCard creditCard;
        try {

            user = userDatabase.selectByUsername(username).get();
            creditCard = creditCardDatabase.selectByNumber(Integer.parseInt(ccnumber)).get();
            if (creditCard.getStatus() == 0) throw new NoSuchElementException(); // se la carta è disabilitata, lancia un'eccezione
            if (creditCard.getOwner() > 0) throw new NoSuchElementException(); // se la carta appartiene a qualcuno, lancia un'eccezione
            CreditCard updatedCreditCard = new CreditCard(creditCard.getId(), creditCard.getNumber(), creditCard.getBalance(), user.getId(), creditCard.getStatus());
            creditCardDatabase.updateById(creditCard.getId(), updatedCreditCard);

            System.out.println("(SellerController) Carta " + ccnumber + " assegnata a " + username);
            return "redirect:/profile/seller?ccsuccess";

        } catch (NoSuchElementException e) {

            System.out.println("(SellerController) Errore: carta non presente o già assegnata.");
            return "redirect:/profile/seller?ccerror";

        }

    }

    @GetMapping("/profile/seller/seller-transactions")
    String profileSellerTransactions(Model model) { /* essenddo un JOIN, faccio uso della vista TransactionUserView */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String storename = userDatabase.selectByUsername(authentication.getName()).get().getStorename();
        List<CreditCardTransactionUserView> creditCardTransactionUserView = transactionDatabase.selectAllJoinUserForSeller(authentication);
        model.addAttribute("creditCardTransactionUserView", creditCardTransactionUserView);
        model.addAttribute("storename", storename);
        return "seller-transactions";
    }

}
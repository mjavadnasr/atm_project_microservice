package ir.payeshgaran.transaction.controller;

import ir.payeshgaran.transaction.model.TransactionModel;
import ir.payeshgaran.transaction.service.TransactionService;
import ir.payeshgaran.transaction.service.imp.TransactionServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public record TransactionController(TransactionServiceImp transactionService) {

    @PostMapping
    public ResponseEntity<String> addTransaction(@RequestBody TransactionModel transactionModel, @RequestHeader("Authorization") String token, HttpServletResponse response) throws IOException {
        transactionService.addTransaction(transactionModel, token, response);
        return ResponseEntity.ok().body("DONE");
    }

    @GetMapping("/get-10-least-transactions/{id}")
    public List<TransactionModel> get10LeastTransactions(@PathVariable Long id) throws IOException {
        return transactionService.get10LeastTransactions(id);
    }
}

package ir.payeshgaran.transaction.service;

import ir.payeshgaran.transaction.model.TransactionModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface TransactionService {
    void addTransaction(TransactionModel transactionModel, String token, HttpServletResponse response) throws IOException;

    List<TransactionModel> get10LeastTransactions(Long id);
}

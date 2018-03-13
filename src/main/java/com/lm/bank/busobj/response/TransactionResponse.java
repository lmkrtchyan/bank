package com.lm.bank.busobj.response;

import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionResponse {
    private final String description;
    private final Long id;
    private final BigDecimal amount;
    private final AccountDto fromAccount;
    private final AccountDto toAccount;
    private final Date date;

    public TransactionResponse(TransactionDto transactionDto, boolean isFromAccount) {
        this.id = transactionDto.getId();
        this.description = transactionDto.getDescription();
        this.amount = transactionDto.getAmount();
        fromAccount = transactionDto.getFromAccount();
        toAccount = transactionDto.getToAccount();
        this.date = transactionDto.getDate();
        if (isFromAccount) {
            fromAccount.setAmount(transactionDto.getFromAccountAmountAfter());
            toAccount.setAmount(null);
        } else {
            fromAccount.setAmount(null);
            toAccount.setAmount(transactionDto.getToAccountAmountAfter());
        }
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public AccountDto getFromAccount() {
        return fromAccount;
    }

    public AccountDto getToAccount() {
        return toAccount;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", amount=" + amount +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", date=" + date +
                '}';
    }
}

package com.lm.bank.service.impl;

import com.lm.bank.busobj.SessionDataContainer;
import com.lm.bank.busobj.dto.AccountDto;
import com.lm.bank.busobj.dto.TransactionDto;
import com.lm.bank.busobj.response.GeneralCreateResponse;
import com.lm.bank.busobj.response.GeneralResponse;
import com.lm.bank.dao.AccountDao;
import com.lm.bank.dao.TransactionDao;
import com.lm.bank.service.TransactionExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionExecutorServiceImpl implements TransactionExecutorService {

    private final TransactionDao transactionDao;

    private final AccountDao accountDao;

    private final SessionDataContainer sessionDataContainer;

    @Autowired
    public TransactionExecutorServiceImpl(TransactionDao transactionDao, AccountDao accountDao, SessionDataContainer sessionDataContainer) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
        this.sessionDataContainer = sessionDataContainer;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public GeneralCreateResponse<Long> executeTransaction(TransactionDto transactionDto) {
        BigDecimal amount = transactionDto.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return GeneralCreateResponse.error("Amount can not be negative");
        }
        Optional<AccountDto> toAccountOpt = accountDao.findById(transactionDto.getToAccountId());
        if (!toAccountOpt.isPresent()) {
            return GeneralCreateResponse.error("Account which is used as receiver error transaction do not exist");
        }
        Long fromAccountId = transactionDto.getFromAccountId();
        if (fromAccountId != null) { //Transaction is not from outside
            Optional<AccountDto> fromAccountOpt = accountDao.findByIdAndOwner(fromAccountId, sessionDataContainer.getUserDto());
            if(!fromAccountOpt.isPresent()) {
                return GeneralCreateResponse.error("Account which is used as sender error transaction do not exist");
            }
            AccountDto from = fromAccountOpt.get();
            BigDecimal fromAmount = from.getAmount();
            if (fromAmount.compareTo(amount) < 0) {
                return GeneralCreateResponse.error("sender account have no much amount for transaction");
            }
            from.setAmount(fromAmount.subtract(amount));
            from = accountDao.save(from);
            transactionDto.setFromAccount(from);
            transactionDto.setFromAccountAmountAfter(from.getAmount());
        }
        AccountDto toAccount = toAccountOpt.get();
        toAccount.setAmount(toAccount.getAmount().add(amount));
        toAccount = accountDao.save(toAccount);
        transactionDto.setToAccount(toAccount);
        transactionDto.setToAccountAmountAfter(toAccount.getAmount());
        transactionDto = transactionDao.save(transactionDto);
        return GeneralCreateResponse.of(transactionDto.getId());
    }
}

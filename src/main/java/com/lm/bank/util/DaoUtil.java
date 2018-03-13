package com.lm.bank.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

@Component
public class DaoUtil {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T executeInTransaction(Callable<T> callable) throws Exception {
        return callable.call();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeInTransaction(RunnableWithException runnable) throws Exception {
        runnable.run();
    }

    public interface RunnableWithException {

        void run() throws Exception;
    }
}

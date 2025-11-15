package pl.lodz.p.it.mateusz.gielczynski.logger;

import org.springframework.transaction.support.TransactionSynchronization;


public class TransactionLifecycleLogger implements TransactionSynchronization {
    private final String transactionId;

    public TransactionLifecycleLogger(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public void afterCompletion(int status) {
        switch (status) {
            case TransactionSynchronization.STATUS_COMMITTED ->
                    LoggingAspect.appendLog("Transaction COMMITED [" + transactionId + "]", LoggingAspect.GREEN);
            case TransactionSynchronization.STATUS_ROLLED_BACK ->
                    LoggingAspect.appendLog("Transaction ROLLED-BACK [" + transactionId + "]", LoggingAspect.RED);
            case TransactionSynchronization.STATUS_UNKNOWN ->
                    LoggingAspect.appendLog("Transaction UNKNOWN-STATE [" + transactionId + "]", LoggingAspect.YELLOW);
        }
        LoggingAspect.endTransaction();
    }
}

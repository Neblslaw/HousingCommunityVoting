package pl.lodz.p.it.mateusz.gielczynski.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class TransactionLoggingAspect {


    @Before(" @annotation(transactional)")
    public void logTransactionStart(JoinPoint joinPoint, Transactional transactional) {
        LoggingAspect.startTransaction();
        String id = UUID.randomUUID().toString().substring(0, 8);

        String sb = (transactional.readOnly() ? "RO" : "RW") + " transaction [" + id + "] started for: " +
                joinPoint.getSignature().toShortString() +
                "  With propagation:" + transactional.propagation();

        TransactionSynchronizationManager.registerSynchronization(new TransactionLifecycleLogger(id));
        LoggingAspect.appendLog(TransactionSynchronizationManager.getCurrentTransactionName(), LoggingAspect.CYAN);

        LoggingAspect.appendLog(sb, transactional.readOnly() ? LoggingAspect.CYAN : LoggingAspect.PURPLE);
    }
}
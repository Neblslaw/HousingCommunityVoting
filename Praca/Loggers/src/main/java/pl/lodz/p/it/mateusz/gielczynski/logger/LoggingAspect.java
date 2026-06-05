package pl.lodz.p.it.mateusz.gielczynski.logger;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class LoggingAspect {
    public static final ThreadLocal<StringBuilder> logBuilder = ThreadLocal.withInitial(StringBuilder::new);
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    private static final String RESET = "\u001B[0m";

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private static final ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Integer> currentTransactions = ThreadLocal.withInitial(() -> 1);
    private static final String INDENT = "    ";

    public static String indent() {
        return INDENT.repeat(callDepth.get());
    }

    public static void appendLog(String message) {
        logBuilder.get().append(indent()).append(message).append("\n");
    }

    public static void appendLog(String message, String color) {
        logBuilder.get().append(indent()).append(color).append(message).append(RESET).append("\n");
    }

    private static void logCompleteRequest() {
        appendLog(Instant.now().toString());
        appendLog("=== REQUEST END ===", YELLOW);
        if (log.isInfoEnabled()) {
            log.info(logBuilder.get().toString());
        }
        callDepth.remove();
        currentTransactions.remove();
        logBuilder.get().setLength(0);
    }

    private static void logCompleteScheduledTask() {
        appendLog(Instant.now().toString());
        appendLog("\n=== SCHEDULED TASK FINISHED ===", BLUE);
        if (log.isInfoEnabled()) {
            log.info(logBuilder.get().toString());
        }
        callDepth.remove();
        currentTransactions.remove();
        logBuilder.get().setLength(0);
    }

    public static void startTransaction() {
        currentTransactions.set(currentTransactions.get() + 1);
    }

    public static void endTransaction() {
        currentTransactions.set(currentTransactions.get() - 1);
        if (currentTransactions.get() == 0) {
            logCompleteRequest();
        }
    }

    public static void logScheduledStart() {
        appendLog("\n=== SCHEDULED TASK STARTED ===", BLUE);
        appendLog(Instant.now().toString());
    }

    @Before("@annotation(scheduled)")
    public void logScheduledStart(JoinPoint joinPoint, Scheduled scheduled) {
        appendLog("\n=== SCHEDULED TASK STARTED ===", BLUE);
        appendLog(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        appendLog(Instant.now().toString());
    }

    @AfterReturning("@annotation(scheduled)")
    public void logScheduledEnd(JoinPoint joinPoint, Scheduled scheduled) {
        logCompleteScheduledTask();
    }


    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logControllerStart(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth != null ? auth.getName() : "anonymous";


        appendLog("\n=== REQUEST START ===", YELLOW);
        appendLog(Instant.now().toString());
        appendLog("User: " + user);
        appendLog("Endpoint: " + request.getMethod() + " " + request.getRequestURI());

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] maskedArgs = maskSensitiveArguments(method, joinPoint.getArgs());
        appendLog(joinPoint.getSignature().toShortString() + " Arguments: " + Arrays.toString(maskedArgs));
    }

    @AfterReturning(pointcut = "within(@org.springframework.web.bind.annotation.RestController *)", returning = "result")
    public void logControllerEnd(JoinPoint joinPoint, Object result) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (isSensitive(method, result)) {
            appendLog("Response: ******-***");
        } else {
            appendLog("Response: " + result);
        }
        endTransaction();
    }

    @AfterReturning(pointcut = "within(@org.springframework.web.bind.annotation.RestControllerAdvice *)", returning = "result")
    public void logControllerException(JoinPoint joinPoint, Object result) {
        appendLog("Response: " + result);
        endTransaction();
    }

    @Around("execution(@pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged * *(..)) || within(@pl.lodz.p.it.mateusz.gielczynski.logger.MethodCallLogged *)")
    public Object logServiceAndRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        callDepth.set(callDepth.get() + 1);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] maskedArgs = maskSensitiveArguments(method, joinPoint.getArgs());

        appendLog("Method: " + methodName + " Arguments: " + Arrays.toString(maskedArgs));
        try {
            Object result = joinPoint.proceed();
            if (isSensitive(method, result)) {
                appendLog("Returned: ******-***");
            } else {
                appendLog("Returned: " + result);
            }
            decreaseCallDepth();
            return result;
        } catch (Exception e) {
            UUID uuid = UUID.randomUUID();
            appendLog("Exception [" + uuid + "] in method " + methodName + ": " + e.getClass(), RED);
            log.error("Exception [" + uuid + "] : " + e);
            decreaseCallDepth();
            throw e;
        }

    }

    private void decreaseCallDepth() {
        if (callDepth.get() > 0) {
            callDepth.set(callDepth.get() - 1);
        }
    }

    private Object[] maskSensitiveArguments(Method method, Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }

        Object[] maskedArgs = Arrays.copyOf(args, args.length);
        Parameter[] parameters = method.getParameters();
        int bound = Math.min(maskedArgs.length, parameters.length);

        for (int i = 0; i < bound; i++) {
            if (parameters[i].isAnnotationPresent(Sensitive.class)
                    || (maskedArgs[i] != null && maskedArgs[i].getClass().isAnnotationPresent(Sensitive.class))) {
                maskedArgs[i] = "******-***";
            }
        }

        return maskedArgs;
    }

    private boolean isSensitive(Method method, Object value) {
        return method.isAnnotationPresent(Sensitive.class)
                || (value != null && value.getClass().isAnnotationPresent(Sensitive.class));
    }
}
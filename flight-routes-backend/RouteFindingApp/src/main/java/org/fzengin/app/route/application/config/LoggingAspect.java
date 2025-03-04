package org.fzengin.app.route.application.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* org.fzengin.app.route..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        logger.error("Exception in {}: {}", joinPoint.getSignature().toShortString(), ex.getMessage(), ex);
    }
    @Before("execution(* org.fzengin.app.route..*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        logger.info("Method called: {}", joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* org.fzengin.app.route..*.*(..))", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        logger.info("Method returned: {} with value {}", joinPoint.getSignature().toShortString(), result);
    }

}

package com.energymonito.user_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {
    @Pointcut("execution(* com.energymonito.user_service.controller.*.*(..))")
    public void controllerMethods(){}

    @Around("controllerMethods()")
    public Object measureExcutionTime(ProceedingJoinPoint joinPoint){
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            long endTime = System.nanoTime();
            long elapsedNs = endTime -start;
            String signature = joinPoint.getSignature().toShortString();
            log.info("controller method {} executed in {} ms",signature, TimeUnit.NANOSECONDS.toMillis(elapsedNs));

        }
    }
}

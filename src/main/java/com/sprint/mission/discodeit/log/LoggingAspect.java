package com.sprint.mission.discodeit.log;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Pointcut("execution(* com.sprint.mission.discodeit.service..*.*(..))")
  public void serviceLayerPointcut() {
  }


  @Pointcut("execution(* com.sprint.mission.discodeit.controller..*.*(..))")
  public void controllerLayerPointcut() {
  }

  @Before("serviceLayerPointcut() || controllerLayerPointcut()")
  public void logBefore(JoinPoint joinPoint) {
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.debug("==> {}.{}({})", className, methodName, Arrays.toString(args));
  }

  @AfterReturning(pointcut = "serviceLayerPointcut() || controllerLayerPointcut()",
      returning = "result")
  public void logAfter(JoinPoint joinPoint, Object result) {
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.debug("==> {}.{}({}), return {} ", className, methodName, Arrays.toString(args), result);
  }

  @Around("serviceLayerPointcut()")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();

    StopWatch watch = new StopWatch();
    watch.start();

    try {
      Object result = joinPoint.proceed();
      watch.stop();
      long executionTime = watch.getTotalTimeMillis();

      log.info("{}.{}, execution time: {} ms", className, methodName, executionTime);
      return result;
    } catch (Throwable throwable) {
      watch.stop();
      long executionTime = watch.getTotalTimeMillis();
      log.error("{}.{}, execution time: {} ms", className, methodName, executionTime, throwable);
     throw throwable;
    }
  }


}

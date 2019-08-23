package com.simonschuster.pimsleur.unlimited.aop;


import com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
public class LogCostTimeAspect {

  private final Logger LOG = LoggerFactory.getLogger(LogCostTimeAspect.class);

  @Around("@annotation(com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime)")
  public Object aroundPrintCostTime(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    LogCostTime logCostTime = signature.getMethod()
        .getAnnotation(LogCostTime.class);
    String className = signature.getDeclaringType().getSimpleName();
    String methodName =
        StringUtils.isEmpty(logCostTime.value()) ? signature.getName() : logCostTime.value();
    long startTimeMillis = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long execTimeMillis = System.currentTimeMillis() - startTimeMillis;
    LOG.info("{}@{} execute cost : {}ms", className, methodName, execTimeMillis);
    return result;
  }
}

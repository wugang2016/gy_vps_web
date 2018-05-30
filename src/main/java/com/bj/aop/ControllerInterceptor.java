package com.bj.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 记录方法参数,并监控方法执行时间
 */
@Aspect
@Component
public class ControllerInterceptor {
    private Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Around("execution(* com.bj..*Controller..*(..))")
    public Object logServiceAccess(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        String className = pjp.getTarget().getClass().getName();
        String fullMethodName = className + "." + pjp.getSignature().getName();

        boolean needLog = true;

        //记录参数,并对参数进行校验
        if (!className.contains("com.sun.proxy.$Proxy") && !className.contains("$$EnhancerBySpringCGLIB$$")) {
            needLog = true;
            if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
                StringBuilder sb = new StringBuilder("调用 ").append(fullMethodName).append("(");
                for (Object arg : pjp.getArgs()) {
                    sb.append(arg == null ? "null" : arg.toString()).append(",");
                }
                sb.setLength(sb.length() - 1);
                sb.append(")");
                log_info(sb.toString());
            } else {
            	log_info("调用 " + fullMethodName + "()");
            }
        }

        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            long elapsedMilliseconds = end - start;
            logger.error(fullMethodName + " 执行异常，耗时: " + elapsedMilliseconds + " 毫秒", e);
            throw e;
        }

        long end = System.currentTimeMillis();
        long elapsedMilliseconds = end - start;
        if (needLog) {
        	log_info(fullMethodName + " 执行结束，耗时: " + elapsedMilliseconds + " 毫秒");
        }

        return result;
    }
    
    private void log_info(String log){
    	if(log.contains("getEntity") 
    			|| log.contains("getStatus")) {
    		//不打印log
    	}else {
            logger.info(log);
    	}
    }
}

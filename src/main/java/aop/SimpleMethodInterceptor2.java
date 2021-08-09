package aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author skywalker
 */
public class SimpleMethodInterceptor2 implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("SimpleMethodInterceptor2被调用: " + invocation.getMethod().getName());
        return invocation.proceed();
    }

}

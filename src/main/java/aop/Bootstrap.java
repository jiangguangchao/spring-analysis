package aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * AOP测试启动类.
 *
 * @author skywalker
 */
public class Bootstrap {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        SimpleAopBean bean = context.getBean(SimpleAopBean.class);
        bean.testB();
        System.out.println(bean.getClass().getSimpleName());
        Advised advised = (Advised)bean;
        Advisor[] arr = advised.getAdvisors();
        for (Advisor adj : arr) {
            System.out.println(adj);
        }

    }

}

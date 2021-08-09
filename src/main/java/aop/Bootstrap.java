package aop;

import controller.UserCtrl;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.UserService;

/**
 * AOP测试启动类.
 *
 * @author skywalker
 */
public class Bootstrap {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("aop/config.xml");
        SimpleAopBean bean = context.getBean(SimpleAopBean.class);
        bean.testB();
        System.out.println(bean.getClass().getSimpleName());
        Advised advised = (Advised)bean;
        Advisor[] arr = advised.getAdvisors();
//        for (Advisor adj : arr) {
//            System.out.println(adj);
//        }

        UserCtrl userCtrl = context.getBean(UserCtrl.class);
        UserService userService = context.getBean(UserService.class);
        System.out.println("userService:" + userService);
        Advised serviceAdvised = (Advised)userService;
        Advisor[] serviceAdvisors = serviceAdvised.getAdvisors();
        for (Advisor a : serviceAdvisors) {
            System.out.println(a);
        }

    }

}

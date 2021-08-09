package controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: spring
 * @description:
 * @author:
 * @create: 2021-08-09 15:40
 */

@Controller
@Transactional
public class UserCtrl {

    public void saveUser() {
        System.out.println("保存用户接口");
    }
}

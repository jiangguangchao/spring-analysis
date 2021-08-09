package service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: spring
 * @description:
 * @author:
 * @create: 2021-08-09 15:38
 */

@Service
@Transactional
public class UserService {

    public void saveUser() {
        System.out.println("保存用户");
    }
}

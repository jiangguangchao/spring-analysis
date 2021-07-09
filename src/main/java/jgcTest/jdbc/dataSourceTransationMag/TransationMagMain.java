package jgcTest.jdbc.dataSourceTransationMag;

import jgcTest.bean.User;
import jgcTest.jdbc.dataSourceTransationMag.dao.UserDao;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * @program: spring
 * @description:
 * @author:
 * @create: 2021-07-08 08:23
 */
public class TransationMagMain {
    private static final Logger log = Logger.getLogger(TransationMagMain.class);
    @Test
    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dstm.getTransaction(transactionDefinition);
        UserDao userDao = context.getBean(UserDao.class);

        User u = new User("jgc3","hn",33);
        try {
            userDao.addUser(u);
        } catch (Exception e) {
            log.error("新增用户过程发生异常,回滚",e);
            dstm.rollback(transactionStatus);
            return;
        }
        log.info("新增完成，开始提交事务");
        dstm.commit(transactionStatus);
    }

    @Test
    public void test2()  {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        User u = new User("jgc7","hn",33);
        UserDao userDao = context.getBean(UserDao.class);
        try {
            userDao.addUser(u);
        } catch (Exception e) {
            log.error("新增用户过程发生异常",e);
            //dstm.commit(transactionStatus);
            //dstm.rollback(transactionStatus);
            return;
        }

    }

    /**
     * 如果connect 将自动提交设置为false，则执行过sql后要 使用connect.commit()进行手动提交
     * 如果不commit，则前面的改动不会被提交。
     */
    @Test
    public void test3() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSource datasource = context.getBean(DataSource.class);
        try {
            Connection con = datasource.getConnection();
            con.setAutoCommit(false);
//            PreparedStatement ps = con.prepareStatement("update user set username = ? where id = ?");
//            ps.setString(1,"updateJGC123");
//            ps.setInt(2, 3);
            PreparedStatement ps = con.prepareStatement("insert into user (username,age,address) values (?,?,?)");
            ps.setString(1,"myname");
            ps.setInt(2,31);
            ps.setString(3,"bj");
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            log.error("获取connect异常",e);
        }
    }


    /**
     * addUser2()方法使用两种方式新增用户，一种是通过jdbcTemplate.getDataSource().getConnection() 获取connect，
     * 然后通过connect新增操作，另一种是通过jdbcTemplate新增
     * 最终只有jdbcTemplate的发生了回退，通过connect的新增操作并没有回退，这是为什么呢？
     * 因为test4()这个方法，开头处使用DataSourceTransactionManager 开启了事务，这种方式开启事务后，会把connect放置到当前
     * 线程中，而jdbcTemplate的操作中会从当前线程获取刚才存入的connect，而发生异常，回退操作的时候用到的也是这个connect。
     * 而jdbcTemplate.getDataSource().getConnection()不是从当前线程中获取connect，这里会从连接池获取连接，显然跟线程中的不是
     * 同一个connect，所以回退对这个connect没有影响。
     */
    @Test
    public void test4()  {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dstm.getTransaction(transactionDefinition);
        UserDao userDao = context.getBean(UserDao.class);
        try {
            userDao.addUser2();
        } catch (Exception e) {
            log.error("新增用户过程发生异常，回退操作",e);
            dstm.rollback(transactionStatus);
            return;
        }
        log.info("提交操作");
        dstm.commit(transactionStatus);

    }


    @Test
    public void test5()  {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        UserDao userDao = context.getBean(UserDao.class);
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus1 = dstm.getTransaction(transactionDefinition);
        User u1 = new User("u111111","111111",11);

        try {
            userDao.addUser(u1);
        } catch (SQLException throwables) {
            log.error("新增用户u1异常", throwables);
            dstm.rollback(transactionStatus1);
            return;
        }

        TransactionStatus transactionStatus2 = dstm.getTransaction(transactionDefinition);
        User u2 = new User("u222222","222222",22);

        try {
            userDao.addUser(u2);
        } catch (SQLException throwables) {
            log.error("新增用户u2异常", throwables);
            dstm.rollback(transactionStatus2);
            return;
        }

        log.info("提交transactionStatus2");
        dstm.commit(transactionStatus2);

    }

    @Test
    public void test6()  {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        UserDao userDao = context.getBean(UserDao.class);
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus1 = dstm.getTransaction(transactionDefinition);
        User u1 = new User("u111111","111111",11);

        try {
            userDao.addUser(u1);
        } catch (SQLException throwables) {
            log.error("新增用户u1异常", throwables);
            dstm.rollback(transactionStatus1);
            return;
        }

        TransactionStatus transactionStatus2 = dstm.getTransaction(transactionDefinition);
        User u2 = new User("u222222","222222",22);

        try {
            userDao.addUser(u2);
        } catch (SQLException throwables) {
            log.error("新增用户u2异常", throwables);
            dstm.rollback(transactionStatus2);
            return;
        }

        log.info("提交transactionStatus1");
        dstm.commit(transactionStatus1);

    }


    @Test
    public void test7()  {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        UserDao userDao = context.getBean(UserDao.class);
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus1 = dstm.getTransaction(transactionDefinition);
        User u1 = new User("u777777","777777",11);

        try {
            userDao.addUser(u1);
        } catch (SQLException throwables) {
            log.error("新增用户u1异常", throwables);
            dstm.rollback(transactionStatus1);
            return;
        }

        transactionDefinition.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus2 = dstm.getTransaction(transactionDefinition);
        User u2 = new User("u777777_2","777777_2",22);

        try {
            userDao.addUser(u2);
        } catch (SQLException throwables) {
            log.error("新增用户u2异常", throwables);
            dstm.rollback(transactionStatus2);
            return;
        }

        log.info("提交transactionStatus2");
        dstm.commit(transactionStatus2);

    }

    @Test
    public void test8()  {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        UserDao userDao = context.getBean(UserDao.class);
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus1 = dstm.getTransaction(transactionDefinition);
        User u1 = new User("u8888888","8888888",11);

        try {
            userDao.addUser(u1);
        } catch (SQLException throwables) {
            log.error("新增用户u1异常", throwables);
            dstm.rollback(transactionStatus1);
            return;
        }

        transactionDefinition.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus2 = dstm.getTransaction(transactionDefinition);
        User u2 = new User("u8888888_2","8888888_2",22);

        try {
            userDao.addUser(u2);
        } catch (SQLException throwables) {
            log.error("新增用户u2异常", throwables);
            dstm.rollback(transactionStatus2);
            return;
        }

        log.info("提交transactionStatus1");
        dstm.commit(transactionStatus1);

    }

    @Test
    public void test9() {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("jgcTest/jdbc/dataSourcetransationMag.xml");
        DataSourceTransactionManager dstm = (DataSourceTransactionManager) context.getBean("dstm");
        UserDao userDao = context.getBean(UserDao.class);
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus1 = dstm.getTransaction(transactionDefinition);

        Object obj = TransactionSynchronizationManager.getResource(dstm.getDataSource());

        User u = new User("userOutRunnable","cccccc",3);
        try {
            userDao.addUser(u);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            dstm.rollback(transactionStatus1);
            return;
        }

        Runnable r = () -> {
            TransactionStatus transactionStatus2 = dstm.getTransaction(transactionDefinition);
            User u2 = new User("userInRunnable","cccccc",3);
            try {
                userDao.addUser(u2);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                dstm.rollback(transactionStatus2);
            }
            dstm.commit(transactionStatus2);
        };

        Thread t = new Thread(r);
        t.start();
        //这种会有个问题，这两个commit 哪一个都可能先执行
        dstm.commit(transactionStatus1);
    }

}

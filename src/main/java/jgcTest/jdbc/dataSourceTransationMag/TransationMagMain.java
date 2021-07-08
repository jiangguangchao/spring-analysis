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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            log.error("新增用户过程发生异常,依然提交",e);
            //dstm.commit(transactionStatus);
            //dstm.rollback(transactionStatus);
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
}

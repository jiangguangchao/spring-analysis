package jgcTest.jdbc.dataSourceTransationMag.dao;

import jgcTest.bean.User;
import org.springframework.core.NamedThreadLocal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: spring
 * @description:
 * @author:
 * @create: 2021-07-08 08:30
 */
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) throws SQLException {
//        DataSource datasource = jdbcTemplate.getDataSource();
//        Connection connection = datasource.getConnection();
//        ConnectionHolder holder = new ConnectionHolder(connection);
        //TransactionSynchronizationManager.bindResource(datasource, holder);

//        ThreadLocal<Map<Object, Object>> resources =
//                new NamedThreadLocal<Map<Object, Object>>("Transactional resources");
//        Map<Object, Object> map = new HashMap<Object, Object>();
//        map.put(datasource,holder);
//        resources.set(map);

        jdbcTemplate.update("insert into user (username,age,address) values (?,?,?)",
                new Object[]{user.getUsername(),user.getAge(),user.getAddress()});
//        Connection con2 = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
//        if (con2 == connection) {
//            System.out.println("同一个connect");
//        } else {
//            System.out.println("不是同一个connect");
//        }

//        DataSource ds1 = jdbcTemplate.getDataSource();
//        DataSource ds2 = jdbcTemplate.getDataSource();
//        if (ds1 == ds2) {
//            System.out.println("ds1 == ds2");
//        } else {
//            System.out.println("ds1 != ds2");
//        }
    }

    /**
     *
     * @throws SQLException
     */
    public void addUser2() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = connection.prepareStatement("insert into user (username,age,address) values (?,?,?)");
        ps.setString(1,"addUserWithConnect");
        ps.setInt(2,22);
        ps.setString(3,"ccccc");
        ps.executeUpdate();

        User u = new User("addUserWithJDBC","jjjj",11);
        addUser(u);

        System.out.println(1/0);//抛出异常

    }

    public void updateUser(User user)  {
        jdbcTemplate.update("update user set username=?,age = ?, address=?",
                new Object[]{user.getUsername(),user.getAge(),user.getAddress()});

    }

    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> userList = jdbcTemplate.queryForList("select * from user");
        return userList;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

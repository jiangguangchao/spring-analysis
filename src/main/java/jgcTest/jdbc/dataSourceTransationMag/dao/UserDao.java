package jgcTest.jdbc.dataSourceTransationMag.dao;

import jgcTest.bean.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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
        Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
        con.setAutoCommit(false);
        jdbcTemplate.update("insert into user (username,age,address) values (?,?,?)",
                new Object[]{user.getUsername(),user.getAge(),user.getAddress()});
        Connection con2 = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
        if (con == con2) {
            System.out.println("con == con2,autoCommit:" + con2.getAutoCommit());
        } else {
            System.out.println("con != con2,autoCommit:" + con2.getAutoCommit());
        }

        DataSource ds1 = jdbcTemplate.getDataSource();
        DataSource ds2 = jdbcTemplate.getDataSource();
        if (ds1 == ds2) {
            System.out.println("ds1 == ds2");
        } else {
            System.out.println("ds1 != ds2");
        }
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

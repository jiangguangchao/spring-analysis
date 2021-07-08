package jgcTest.bean;

/**
 * @program: spring
 * @description:
 * @author:
 * @create: 2021-07-08 08:39
 */
public class User {
    private Integer id;
    private String username;
    private String address;
    private Integer age;

    public User(String username, String address,Integer age) {
        this.username = username;
        this.address = address;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

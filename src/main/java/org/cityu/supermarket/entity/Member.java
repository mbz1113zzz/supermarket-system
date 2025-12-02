package org.cityu.supermarket.entity;

import java.io.Serializable;

/**
 * Member entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class Member implements Serializable {
    /**
     * Maps to the memberid column in DB
     */
    private String memberId;

    private String password;

    private String name;

    private String sex;

    private String birthday;

    private static final long serialVersionUID = 1L;

    public String getmemberId() {
        return memberId;
    }

    public void setmemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
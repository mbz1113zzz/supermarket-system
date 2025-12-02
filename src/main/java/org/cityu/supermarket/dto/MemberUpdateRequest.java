package org.cityu.supermarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating member info
 */
public class MemberUpdateRequest {

    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Sex must not be empty")
    private String sex;

    @NotBlank(message = "Birthday must not be empty")
    private String birthday;

    @Size(min = 6, max = 64, message = "Password length must be between 6 and 64 characters")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

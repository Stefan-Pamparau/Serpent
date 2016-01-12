package model;

/**
 * Created by stefan.pamparau on 1/12/2016.
 */
public class Client {
    private String firstName;
    private String surname;
    private String address;
    private String email;
    private String phone;
    private String sex;
    private Integer age;

    public Client(String email) {
        this.email = email;
    }

    public Client(String firstName, String surname, String address, String email, String phone, String sex, Integer age) {
        this(email);
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.sex = sex;
        this.age = age;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

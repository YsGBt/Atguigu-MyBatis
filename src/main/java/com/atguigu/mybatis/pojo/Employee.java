package com.atguigu.mybatis.pojo;

public class Employee {

  private Integer eid;
  private String employeeName;
  private Integer age;
  private String gender;
  private String email;
  private Department department;

  public Employee(Integer eid, String employeeName, Integer age, String gender, String email) {
    this.eid = eid;
    this.employeeName = employeeName;
    this.age = age;
    this.gender = gender;
    this.email = email;
  }

  public Employee() {
  }

  public Integer getEid() {
    return eid;
  }

  public void setEid(Integer eid) {
    this.eid = eid;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "eid=" + eid +
        ", employeeName='" + employeeName + '\'' +
        ", age=" + age +
        ", gender='" + gender + '\'' +
        ", email='" + email + '\'' +
        ", department=" + department +
        '}';
  }
}

package com.atguigu.mybatis.pojo;

import java.util.List;

public class Department {

  private Integer did;
  private String departmentName;
  private List<Employee> employeeList;

  public Department(Integer did, String departmentName) {
    this.did = did;
    this.departmentName = departmentName;
  }

  public Department() {
  }

  public Integer getDid() {
    return did;
  }

  public void setDid(Integer did) {
    this.did = did;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public List<Employee> getEmployeeList() {
    return employeeList;
  }

  public void setEmployeeList(List<Employee> employeeList) {
    this.employeeList = employeeList;
  }

  @Override
  public String toString() {
    return "Department{" +
        "did=" + did +
        ", departmentName='" + departmentName + '\'' +
        ", employeeList=" + employeeList +
        '}';
  }
}

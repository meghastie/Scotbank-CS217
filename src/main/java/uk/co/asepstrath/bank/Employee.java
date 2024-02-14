package uk.co.asepstrath.bank;

public class Employee {
    private int id;
    private String name;
    private int age;
    private String address;
    private double salary;
    private java.sql.Date date;

    public Employee(int id,String name,int age, String address, double salary){
        id = this.id;
        name = this.name;
        age = this.age;
        address = this.address;
        salary = this.salary;
    }
}

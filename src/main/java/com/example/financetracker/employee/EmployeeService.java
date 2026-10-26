package com.example.financetracker.employee;

import java.util.List;
import java.util.Optional;

public class EmployeeService{

   private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(long employeeId){
        return employeeRepository.findById(employeeId);
    }

    public void deleteEmployee(long employeeId){
        employeeRepository.deleteById(employeeId);
    }
}

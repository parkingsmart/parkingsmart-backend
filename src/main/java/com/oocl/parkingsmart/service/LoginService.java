package com.oocl.parkingsmart.service;
import com.oocl.parkingsmart.entity.Employee;
import com.oocl.parkingsmart.entity.User;
import com.oocl.parkingsmart.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private EmployeeRepository employeeRepository;
    public Employee loginAuthentication(String username,String password) {
        Employee employee = null;
        if(username.indexOf("@") > 0){
            employee = employeeRepository.findByEmail(username);
            return employee!=null?((employee.getPassword().equals(password))?employee:null):null;
        }else if(username.length() == 11){
            employee = employeeRepository.findByPhone(username);
            return employee!=null?((employee.getPassword().equals(password))?employee:null):null;
        }else {
            Optional<Employee> res = employeeRepository.findById(Long.parseLong(username));
            if(res.isPresent()){
                employee = res.get();
                return (employee.getPassword().equals(password))?employee:null;
            }
            return null;
        }
    }
}

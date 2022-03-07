package com.wezaam.withdrawal.domain.repositories;


import com.wezaam.withdrawal.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {


}

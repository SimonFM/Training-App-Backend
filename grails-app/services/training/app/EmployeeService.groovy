package training.app

import grails.transaction.Transactional
import org.apache.commons.lang.StringUtils

@Transactional
class EmployeeService {

    boolean createEmployee(Map params){
        Employee newEmployee = new Employee()
        newEmployee?.firstname = params?.firstname
        newEmployee?.surname = params?.surname
        newEmployee?.jobTitle = params?.jobTitle
        newEmployee?.email = params?.email
        newEmployee?.deleted = false

        newEmployee.validate()
        if (newEmployee.hasErrors()) {
            newEmployee.errors.allErrors.each {
                println it
            }
        } else {
            if(!StringUtils.isBlank(params?.email) && !StringUtils.isBlank(params?.jobTitle) &&
                    !StringUtils.isBlank(params?.surname) && !StringUtils.isBlank(params?.firstname)){
                newEmployee.save(flush : true, failOnError: true)
                return true
            }
        }
        return false
    }

    boolean updateEmployee(Map params){
        Employee employee = getEmployee(params?.get("id") as String)
        if(employee){
            if(params?.get("jobTitle")){
                employee?.jobTitle = params?.get("jobTitle")
            }
            if(params?.get("firstname")){
                employee?.firstname = params?.get("firstname")
            }
            if(params?.get("surname")){
                employee?.surname = params?.get("surname")
            }
            if(params?.get("email")){
                employee?.email = params?.get("email")
            }

            employee.validate()
            if (employee.hasErrors()) {
                employee.errors.allErrors.each {
                    println it
                }
            } else {
                employee.save(flush : true, failOnError: true)
                return true
            }

        }
        return false
    }

    boolean removeEmployee(Map params){
        Employee employee = getEmployee(params.get("id") as String)
        if(employee){
            employee.deleted = true
            return true
        } else {
            return false
        }
    }

    Employee findEmployee(Map params){
        Employee employee = getEmployee(params.get("id") as String)

        if(employee && !employee.deleted){
           return employee
        } else {
           return new Employee()
        }
    }

    List<Employee> getAll(){
        List<Employee> result = Employee?.findAll() { deleted == false}

        return result
    }

    private Employee getEmployee(String idAsString){
        long id = Long.parseLong(idAsString)
        Employee employee = Employee.findById(id)
        return employee
    }
}

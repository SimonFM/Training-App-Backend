package training.app

import grails.converters.JSON
import org.apache.commons.collections.map.HashedMap
import org.apache.commons.lang.StringUtils

class EmployeeController {

    def EmployeeService

    def index() {
        println "GET all Employee"
        Map result = showAllEmployees() as Map
        Map response = makeAllEmployeesResponse(true, "All Employees", "", result)
        render response as JSON
    }

    private List<Employee> showAllEmployees(){
        EmployeeService.getAll()
    }

    def allEmployees(){
        render Employee.findAll() as JSON
    }

    def show() {
        Map postParams = request.getJSON() as Map
        println "POST show Employee $postParams"

        if(postParams){
            render showEmployee(postParams) as JSON
        } else {
            render makeErrorResponse() as JSON
        }
    }

    private Map showEmployee(Map postParams){
        Map parameters = postParams as Map
        Employee employee = EmployeeService.findEmployee(parameters)
        boolean employeeFound = (!StringUtils.isBlank(employee.surname))
        employeeFound ? employee as Map : new HashedMap()
    }

    def create() {
        Map postParams = request.getJSON()
        println "POST create Employee $postParams"

        if(postParams){
            render createUser(postParams) as JSON
        } else {
            render makeErrorResponse() as JSON
        }
    }

    private Map createUser(Map parameters){
        boolean wasEmployeeCreated = EmployeeService.createEmployee(parameters)
        String name = parameters?.get("email")
        Map response = makeResponse(wasEmployeeCreated, "$name was created", "Unable to create $name")
        response
    }

    def remove(){
        Map parameters = request.getJSON() as Map
        println "POST remove Employee $parameters"

        if(parameters){
            Map response = removeUser(parameters)
            render response as JSON
        } else {
            render makeErrorResponse() as JSON
        }
    }

    private Map removeUser(Map parameters){
        boolean wasEmployeeRemoved = EmployeeService.removeEmployee(parameters)
        String id = parameters.get("id")
        String name = Employee.findById(Long.parseLong(id)).email
        Map response = makeResponse(wasEmployeeRemoved, "$name was removed" , "Unable to remove $name")
        response
    }

    def update() {
        Map parameters = request.getJSON() as Map
        println "POST remove Employee $parameters"

        if(parameters){
            HashedMap response = updateUser(parameters)
            render response as JSON
        } else {
            render makeErrorResponse() as JSON
        }
    }

    private Map updateUser(Map parameters) {
        boolean wasEmployeeUpdated = EmployeeService.updateEmployee(parameters)
        String name = parameters?.get("email")
        Map response = makeResponse(wasEmployeeUpdated, "$name was updated", "Unable to update $name")
        response
    }

    private Map makeAllEmployeesResponse (boolean success, String successMessage, String errorMessage, Map data){
        Map response = makeResponse(success, successMessage, errorMessage)
        response.put("data", data)
        response
    }

    private HashedMap makeResponse(boolean success, String successMessage, String errorMessage) {
        String responseMessage = (success) ? successMessage : errorMessage
        String resultMessage = (success) ? "OK" : "Error"
        String resultCode = (success) ? "200" : "500"
        String message = "$resultMessage - $responseMessage"

        Map response = new HashedMap()
        response.put("result", resultMessage)
        response.put("msg", message)
        response.put("status", resultCode)
        response
    }

    private HashedMap makeErrorResponse() {
        Map response = new HashedMap()
        String message = "Unable to process response"
        String status = "500"

        response.put("result", "$message - $status")
        response.put("msg", message)
        response.put("status", status)
        response
    }
}

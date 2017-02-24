package training.app

class Employee {

    String firstname
    String surname
    String jobTitle
    String email
    boolean deleted

    static constraints = {
        firstname blank: false, nullable: false
        surname blank: false, nullable: false
        jobTitle blank: false, nullable: false
        email email: true, blank: false, nullable: false, unique: true
    }
}

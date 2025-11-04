import java.io.BufferedReader;


public class Customer {

    private String firstName;
    private String lastName;
    private String ID;
    BufferedReader br;

    Customer(String firstName, String lastName, String ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
}

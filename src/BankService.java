import java.io.*;
import java.util.*;

public class BankService {
    static Scanner sc = new Scanner(System.in);
    static Map<String, Account> accountMap = new HashMap<>();
    static int nextAvailableID = 0;
    static final  String csvLocation = "src/CustomerData.csv";
    static String currentUser;
    static boolean isUsing = true;

    static void main(String[] args) {
        readCSVFile(csvLocation);
        if(!login(sc)) {
            createAccount(sc);
        }
        Account targetAccount = accountMap.get(currentUser);
        System.out.println("********************");
        System.out.println("Log in successfully!");
        System.out.println("Welcome " + targetAccount.getCustomer().getFullName());
        System.out.println("********************");
        while(isUsing) {
            int choice = displayMenu(sc);
            switch (choice) {
                case 1:
                    System.out.println(targetAccount.getBalance());
                    break;
                case 2:
                    while (true) {
                        System.out.print("How much would you like to deposit?: ");
                        String depositStr = sc.next();
                        double depositAmount = 0;
                        boolean despositValid = true;
                        try {
                            if (depositStr.contains(".")) {
                                String decimalPart = depositStr.substring(depositStr.indexOf(".") + 1);
                                if (decimalPart.length() > 2) {
                                    System.out.println("Please enter only 2 decimal places");
                                    despositValid = false;
                                }
                            }
                            if (despositValid) {
                                depositAmount = Double.parseDouble(depositStr);
                            } else {
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            // Catches non-numeric input (e.g., "abc")
                            System.out.println("🚫 Invalid input. Please enter a valid number.");
                            continue; // Go back to the start of the while loop
                        }
                        if (depositAmount > 0) {
                            targetAccount.deposit(depositAmount);
                            break;
                        } else {
                            System.out.println("Please enter amount greater than 0.");
                        }
                    }
                    break;
                case 3:
                    while (true) {
                        System.out.print("How much would you like to withdraw?: ");
                        String withdrawStr = sc.next();
                        double withdrawAmount = 0;
                        boolean withdrawValid = true;
                            try{
                               if(withdrawStr.contains(".")) {
                                   String decimalPart = withdrawStr.substring(withdrawStr.indexOf(".") + 1);
                                   if(decimalPart.length() > 2) {
                                       System.out.println("Please enter only 2 decimal places");
                                       withdrawValid = false;
                                   }
                               }
                               if(withdrawValid) {
                                   withdrawAmount = Double.parseDouble(withdrawStr);
                               }
                               else {
                                   continue;
                               }
                            }
                            catch (NumberFormatException e) {
                                // Catches non-numeric input (e.g., "abc")
                                System.out.println("🚫 Invalid input. Please enter a valid number.");
                                continue; // Go back to the start of the while loop
                            }

                            // 1. Corrected Condition: Allows withdrawal up to the full balance
                            if (withdrawAmount > 0 && withdrawAmount <= targetAccount.getBalance()) {
                                targetAccount.withdraw(withdrawAmount); // The method handles the rest
                                break; // Exit the loop on success
                            }
                            else if (withdrawAmount <= 0) {
                                System.out.println("Withdrawal amount must be greater than zero.");
                            }
                            else {
                                // Insufficient funds
                                System.out.println("⚠️ Insufficient funds! Current balance: " + targetAccount.getBalance());
                            }
                        }
                    break;
                case 4:
                    System.out.println("Thank you for using our service.");
                    isUsing = false;
                    break;
            }
        }
        writeDataToCSV();
    }

    public static String generateKey (String firstName, String lastName) {
        return firstName.toLowerCase() + lastName.toLowerCase();
    }

    public static void readCSVFile(String csvLocation) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvLocation))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) {
                    continue;
                }
                String data[] = line.split(",");
                if(data.length != 4) {
                    System.out.println("Invalid CSV Line");
                    continue;
                }
                String firstName = data[0].trim();
                String lastName = data[1].trim();
                String id  = data[2].trim();
                double balance = Double.parseDouble(data[3].trim());

                try {
                    int currentID = Integer.parseInt(id);
                    if (currentID > nextAvailableID) {
                        nextAvailableID = currentID;
                    }
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid ID");
                    continue;
                }

                Customer customer = new Customer(firstName,lastName,id);
                Account account = new Account(customer,balance);

                String key =  generateKey(firstName, lastName);
                accountMap.put(key, account);
            }
        }
        catch (IOException e) {
            System.err.println("Error opening csv file: " + csvLocation);
        }
    }

    static boolean login(Scanner input) {
        System.out.print("Please enter your First Name: ");
        String fnInput = input.nextLine();
        System.out.print("Please enter your Last Name: ");
        String fullnameInput = fnInput + input.nextLine();
        boolean islogin = false;
        for(Map.Entry<String, Account> entry : accountMap.entrySet()) {
            if (entry.getKey().equals(fullnameInput)) {
                islogin = true;
                currentUser = fullnameInput;
                break;
            }
        }
        return islogin;
    }

    static void createAccount(Scanner input) {
        System.out.println("not in the system");
        System.out.println("Lets create you an account");
        System.out.print("Enter first name: ");
        String firstName = sc.next();
        System.out.print("Enter last name: ");
        String lastName = sc.next();
        Customer customer = new Customer(firstName, lastName,Integer.toString(nextAvailableID + 1));
        Account account = new Account(customer);
        currentUser = firstName + lastName;
        accountMap.put(firstName+lastName, account);
    }

    static int displayMenu(Scanner input) {
        int choice = 0;
        while(true) {
            try {
                System.out.println("1.Show Balance");
                System.out.println("2.Deposit");
                System.out.println("3.Withdraw");
                System.out.println("4.Quit");
                System.out.print("Please enter your choice: ");
                choice = input.nextInt();
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice");
                }
                else {
                    return choice;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid Input");
            }
        }
    }

    static void writeDataToCSV() {
        List<Account> accountList = new ArrayList<>(accountMap.values());

        Collections.sort(accountList, new AccountIDComparator());

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(csvLocation))) {
            bw.write("firstName,lastName,ID,balance\n");
            for (Account account : accountList) {
                Customer customer = account.getCustomer();

                String line = customer.getFirstName() + "," +
                              customer.getLastName() + "," +
                              customer.getID() + "," +
                              String.format("%.2f",account.getBalance()) + "\n";
                bw.write(line);
            }
        }
        catch (IOException e) {
            System.err.println("Error opening csv file: " + csvLocation);
        }
    }
}

import java.util.Comparator;

public class AccountIDComparator implements Comparator<Account> {
    @Override
    public int compare(Account a1, Account a2) {
        String idStr1 = a1.getCustomer().getID();
        String idStr2 = a2.getCustomer().getID();

        try{
            int id1 = Integer.parseInt(idStr1);
            int id2 = Integer.parseInt(idStr2);
            return Integer.compare(id1,id2);
        }
        catch(NumberFormatException e){
            return 0;
        }
    }
}

public class SMSParser {

    public static void process(String msg) {

        msg = msg.toLowerCase();

        String type = msg.contains("credited") ? "income" : "expense";

        double amount = Double.parseDouble(msg.replaceAll("[^0-9]", ""));

        System.out.println(type + " " + amount);
    }
}
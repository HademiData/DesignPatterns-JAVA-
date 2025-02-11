
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;



class Strategy {

    /*
     * Strategy is a behavioral design pattern that lets you define a family
     *  of algorithms, put each of them into a separate
     *  class, and make their objects interchangeable.
    */

    /*
     * The Strategy pattern suggests that you take a class that does something specific in a 
     * lot of different ways and extract all of these algorithms into separate classes called strategies.
    The original class, called context, must have a field for storing a reference
     to one of the strategies. The context delegates the work to a linked strategy 
     object instead of executing it on its own.

    The context isn’t responsible for selecting an appropriate algorithm
     for the job. Instead, the client passes the desired strategy to the context.
      In fact, the context doesn’t know much about strategies. It works with all 
      strategies through the same generic interface, which only exposes a single 
      method for triggering the algorithm encapsulated within the selected strategy.

    This way the context becomes independent of concrete strategies, 
    so you can add new algorithms or modify existing ones without
     changing the code of the context or other strategies.
     */

    /*
     * Strategy is a behavioral design pattern that turns a set of behaviors 
     * into objects and makes them interchangeable inside original context object.
     */


     // Example app 
     // Payment method in an e-commerce app


        /**
     * Common interface for all strategies.
     */
    public interface PayStrategy {
        boolean pay(int paymentAmount);
        void collectPaymentDetails();
    }

        /**
     * Concrete strategy. Implements PayPal payment method.
     */
    public class PayByPayPal implements PayStrategy {
        private static final Map<String, String> DATA_BASE = new HashMap<>();
        private final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
        private String email;
        private String password;
        private boolean signedIn;

        static {
            DATA_BASE.put("amanda1985", "amanda@ya.com");
            DATA_BASE.put("qwerty", "john@amazon.eu");
        }

            /**
         * Collect customer's data.
         */
        @Override
        public void collectPaymentDetails() {
            try {
                while (!signedIn) {
                    System.out.print("Enter the user's email: ");
                    email = READER.readLine();
                    System.out.print("Enter the password: ");
                    password = READER.readLine();
                    if (verify()) {
                        System.out.println("Data verification has been successful.");
                    } else {
                        System.out.println("Wrong email or password!");
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // helper method
        private boolean verify() {
            setSignedIn(email.equals(DATA_BASE.get(password)));
            return signedIn;
        }
        /**
         * Save customer data for future shopping attempts.
         */
        @Override
        public boolean pay(int paymentAmount) {
            if (signedIn) {
                System.out.println("Paying " + paymentAmount + " using PayPal.");
                return true;
            } else {
                return false;
            }
        }
        
        // helper method
        private void setSignedIn(boolean signedIn) {
            this.signedIn = signedIn;
        }
    }

        /**
     * Concrete strategy. Implements credit card payment method.
     */
    public class PayByCreditCard implements PayStrategy {
        private final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
        private CreditCard card;

        /**
         * Collect credit card data.
         */
        @Override
        public void collectPaymentDetails() {
            try {
                System.out.print("Enter the card number: ");
                String number = READER.readLine();
                System.out.print("Enter the card expiration date 'mm/yy': ");
                String date = READER.readLine();
                System.out.print("Enter the CVV code: ");
                String cvv = READER.readLine();
                card = new CreditCard(number, date, cvv);

                // Validate credit card number...

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        /**
         * After card validation we can charge customer's credit card.
         */
        @Override
        public boolean pay(int paymentAmount) {
            if (cardIsPresent()) {
                System.out.println("Paying " + paymentAmount + " using Credit Card.");
                card.setAmount(card.getAmount() - paymentAmount);
                return true;
            } else {
                return false;
            }
        }

        private boolean cardIsPresent() {
            return card != null;
        }
    }

        /**
     * Dummy credit card class.
     */
    public class CreditCard {
        private int amount;
        private String number;
        private String date;
        private String cvv;

        CreditCard(String number, String date, String cvv) {
            this.amount = 100_000;
            this.number = number;
            this.date = date;
            this.cvv = cvv;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }
    }


        /**
     * Order class. Doesn't know the concrete payment method (strategy) user has
     * picked. It uses common strategy interface to delegate collecting payment data
     * to strategy object. It can be used to save order to database.
     */
    public class Order {
        private int totalCost = 0;
        private boolean isClosed = false;

        public void processOrder(PayStrategy strategy) {
            strategy.collectPaymentDetails();
            // Here we could collect and store payment data from the strategy.
        }

        // setter and getter methods
        public void setTotalCost(int cost) {
            this.totalCost += cost;
        }

        public int getTotalCost() {
            return totalCost;
        }

        public boolean isClosed() {
            return isClosed;
        }

        public void setClosed() {
            isClosed = true;
        }
    }
        
    /**
     * World first console e-commerce application.
     */
    public class Demo {

        private static Map<Integer, Integer> priceOnProducts = new HashMap<>();
        private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // I added this because of the parent class
        static Strategy parentStrategy1  = new Strategy();
        
        private static Order order = parentStrategy1.new Order();
        private static PayStrategy strategy;

        static {
            priceOnProducts.put(1, 2200);
            priceOnProducts.put(2, 1850);
            priceOnProducts.put(3, 1100);
            priceOnProducts.put(4, 890);
        }

        public static void main(String[] args) throws IOException {
            while (!order.isClosed()) {
                int cost;

                String continueChoice;
                do {
                    System.out.print("Please, select a product:" + "\n" +
                            "1 - Mother board" + "\n" +
                            "2 - CPU" + "\n" +
                            "3 - HDD" + "\n" +
                            "4 - Memory" + "\n");
                    int choice = Integer.parseInt(reader.readLine());
                    cost = priceOnProducts.get(choice);
                    System.out.print("Count: ");
                    int count = Integer.parseInt(reader.readLine());
                    order.setTotalCost(cost * count);
                    System.out.print("Do you wish to continue selecting products? Y/N: ");
                    continueChoice = reader.readLine();
                } while (continueChoice.equalsIgnoreCase("Y"));

                if (strategy == null) {
                    System.out.println("Please, select a payment method:" + "\n" +
                            "1 - PalPay" + "\n" +
                            "2 - Credit Card");
                    String paymentMethod = reader.readLine();

                    // Client creates different strategies based on input from user,
                    // application configuration, etc.

                    Strategy parentStrategy = new Strategy();
                    if (paymentMethod.equals("1")) {
                        strategy = parentStrategy.new PayByPayPal();
                    } else {
                        strategy = parentStrategy.new PayByCreditCard();
                    }
                }
                // Order object delegates gathering payment data to strategy object,
                // since only strategies know what data they need to process a
                // payment.
                order.processOrder(strategy);

                System.out.print("Pay " + order.getTotalCost() + " units or Continue shopping? P/C: ");
                String proceed = reader.readLine();
                if (proceed.equalsIgnoreCase("P")) {
                    // Finally, strategy handles the payment.
                    if (strategy.pay(order.getTotalCost())) {
                        System.out.println("Payment has been successful.");
                    } else {
                        System.out.println("FAIL! Please, check your data.");
                    }
                    order.setClosed();
                }
            }
        }
    }


                    





}
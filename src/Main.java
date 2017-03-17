import PayOutClasses.EuroCall;
import Resources.Option;
import middleware.JMSClient;
import middleware.JMSProducer;

/**
 * Created by conorhowells on 11/19/16.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        // Create the two Option channels we require
        String[] channels = new String[]{"EuroCall", "AsianCall"};


        /** Problems 1 and 2: Stopping Criteria = 0.96 estimation error is less than 0.1$ */
        double ProbabilityLevel = 0.96;
        double errorLevel = 0.1;


        // Create the option specifications
        /** Problem 1 - Euro Call Option Price. Price will be outputted to command line.
         * Current Price: 152.35, vol = 0.01, r = 0.0001
         * Days = 252, Strike Price = 165
         */
        // Note: vol = 0.01, r =0.0001 and days = 252 are defaults
        Option euroOption = new Option.OptionBuilder().setStrikePrice(165.0).setCurrentPrice(152.35).createOption();

        /** Problem 2 - Asian Call Option Price. Price will be outputted to command line.
         * Current Price: 152.35, vol = 0.01, r = 0.0001
         * Days = 252, Strike Price = 164
         */
        // Note: vol = 0.01, r =0.0001 and days = 252 are defaults
        Option asianOption =new Option.OptionBuilder().setStrikePrice(164.0).setCurrentPrice(152.35).createOption();

        // Initiation of Server
        JMSProducer server = new JMSProducer();
        server.setOption("EuroCall", euroOption);
        server.setOption("AsianCall", asianOption);
        server.setTolerance(ProbabilityLevel, errorLevel);

        // Initiation of Client for EuroCall
        JMSClient clientEuro = new JMSClient("EuroCall");

        // Initiation of Client for AsianCall
        JMSClient clientAsia = new JMSClient("AsianCall");



        // Generate Server Thread
        Thread serverThread = new Thread(server);
        serverThread.setDaemon(false);
        serverThread.start();


        // Generate Euro Thread
        Thread euroThread = new Thread(clientEuro);
        euroThread.setDaemon(false);
        euroThread.start();

        // Generate Asia Thread
        Thread asiaThread = new Thread(clientAsia);
        asiaThread.setDaemon(false);
        asiaThread.start();




    }
}

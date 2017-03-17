package middleware;

import Interfaces.PayOut;
import PayOutClasses.EuroCall;
import oracle.jvm.hotspot.jfr.Producer;
import org.apache.activemq.*;

import javax.jms.*;

import Resources.Option;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.InactiveDurableSubscriptionView;

import java.util.*;

import Resources.StatsCollector;
import Resources.Tolerance;

/**
 * Created by conorhowells on 11/15/16.
 */
public class JMSProducer implements Runnable{

    private static ConnectionFactory _factory;
    private static String _brokerURL = "tcp://localhost:61616";
    private Connection _connection;
    public Connection getConnection(){return _connection;}
    private Session _session;
    public Session getSession(){return _session;}
    private MessageProducer _producer;


    /** Need option to create a message */
    private Option _option;

    /** payout map */
    private HashMap<String,Integer> _payoutMap;

    /** Tolerance */
    private double _tolerance;
    /** errorLevel */
    private double _errorLevel;

    /** List QueueObjects that store all of the details for a given queue */
    private QueueObject[] _queueObjects;

    /** queueObject currently in use */
    private QueueObject _queueObject;

    /** Counter for queueObject creation */
    private int _counter = 0;

    @Override
    public void run() {
        try {
            simulate();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public JMSProducer() throws Exception{
        //This message broker is embedded
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector(_brokerURL);
        broker.start();
        //Acquire a connection factory
        _factory = new ActiveMQConnectionFactory(_brokerURL);
        // Create a connection
        _connection = _factory.createConnection();
        // Start the connection
        _connection.start();
        // create a Session
        _session = _connection.createSession(false, _session.AUTO_ACKNOWLEDGE);
        // Create a JMS Producer
        _producer = _session.createProducer(null);
        _producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        // Initialize the payoutMap
        _payoutMap = new HashMap<String,Integer>();
        // QueueObjects
        _queueObjects = new QueueObject[2];

    }

    /** Set the option for a given payout */
    public void setOption(String payout, Option newOption) throws Exception{
        _queueObjects[_counter] = new QueueObject(getSession(), payout, newOption);
        _payoutMap.put(payout, _counter);
        _counter = _counter + 1;
    }

    /** Set the Tolerance */
    public void setTolerance(double probabilityLevel, double errorLevel) throws Exception{
        // Set Tolerance and error level for simulation
        Tolerance tolerance = new Tolerance(probabilityLevel, errorLevel);
        _tolerance = tolerance.getTolerance();
        _errorLevel = errorLevel;
    }

    /** Create a message based off the elements of the option
     * Note: Doesn't need payout type because this is known via the channel it is sent to*/
    public MapMessage createMessage() throws Exception{
        MapMessage newMessage = _session.createMapMessage();
        newMessage.setDouble("interest",_option.getInterestRate());
        newMessage.setDouble("volatility", _option.getVolatility());
        newMessage.setDouble("strike", _option.getStrikePrice());
        newMessage.setInt("numOfDays", _option.getNumOfDays());
        newMessage.setDouble("current",_option.getCurrentPrice());
        return newMessage;
    }


    /** We want to be able to send batches of 100 requests to a given queue
     *  while out stopping criteria aren't met
     * @param payout a string of which queue to send to: either "asianCall" or "euroCall"
     *               i.e. look at generateQueues method
     */
    public void generateBatch(String payout) throws Exception{
        // Batch should be 100 requests
        int cnt = 100;
        // Let's start to generate messages
        for ( int i = 0; i < cnt; i++) {
            MapMessage msg = createMessage();
            _producer.send(_queueObject.getQueue(), msg);

        }
//        System.out.println( "Batch Sent");
        // Let's listen to responses
        for (int i = 0; i<cnt; i++) {
            MapMessage returnMessage = (MapMessage) _queueObject.getConsumer().receive();
//                System.out.println(returnMessage.getDouble("payout"));
            _queueObject.getStatsCollector().addDouble(returnMessage.getDouble("payout"));

        }
//        System.out.println("Batch Received");
//            Thread.sleep(300);

    }

    /** Reset method for an individual simulation */
    public void resetChannel(int channelNumber){
        _queueObjects[channelNumber].getStatsCollector().reset();
        _queueObject.setStop(true);
    }

    /** Method that initiates a simulation, and decides if it should ask for another batch of requests from the clients
     * for a given probability and error Level
     * @throws Exception
     */
    public void simulate() throws Exception{
        Set<String> keySet = _payoutMap.keySet();
        Iterator<String> keysIt = keySet.iterator();


        // Loop through Options on Server
        while(keysIt.hasNext()){
            String payout = keysIt.next();
            int payout_location = _payoutMap.get(payout).intValue();
            _queueObject = _queueObjects[payout_location];
            _option = _queueObject.getOption();
            //continue to produce batches until tolerance of stats is satisfied
            System.out.println("Payout for "+payout+" is being generated");
            Long startTime = System.currentTimeMillis();
            while(_queueObject.getStop().booleanValue()) {
                generateBatch(_queueObject.getPayOut());

                //check tolerance of stats collecto every 100 and stop batch requests if proper levels are reached
                double numOfSimulations = _queueObject.getStatsCollector().getNumOfDoubles();
                if ((numOfSimulations > 1000) &&
                        (((_tolerance * _queueObject.getStatsCollector().getStdDev() /
                                Math.sqrt(_queueObject.getStatsCollector().getNumOfDoubles())) - _errorLevel) <= 0)) {
                    _queueObject.setStop(false);
                }
            }
//            TextMessage msg = _session.createTextMessage("complete");
//            _producer.send(_queueObject.getQueue(),msg);
            System.out.println("Simulation of "+payout+" channel complete.");
            System.out.println("Time taken (millis): " + (System.currentTimeMillis() - startTime));
            System.out.println("Number of Iterations: " + _queueObject.getStatsCollector().getNumOfDoubles());
            System.out.println("Standard Deviation of Stock Path: " + _queueObject.getStatsCollector().getStdDev());
            System.out.println("Option Price: " + _queueObject.getStatsCollector().getMean());
            _queueObject.getConsumer().close();

        }

        _producer.close();
        _session.close();
        _connection.stop();
        System.exit(1);

    }

}

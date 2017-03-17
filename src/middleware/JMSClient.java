package middleware;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by conorhowells on 11/15/16.
 */
public class JMSClient implements Runnable {


    private static ConnectionFactory _factory;
    private static String _brokerURL = "tcp://localhost:61616";
    //    private static String _brokerURL = "tcp://localhost:61616"; // "vm://localhost";
    private Connection _connection;
    public Connection getConnection(){return _connection;}
    private Session _session;
    private String _optionType;

    /** Produces the answers and sends them to Topic */
    private MessageProducer _producer;
    /** Receives the requests from queue */
    private MessageConsumer _consumer;


    /** Stock Path Creation */
    private StockPathObject _stockPathObject;



    /** Taken from Eran's notes and ActiveMQClient.java code */
    public JMSClient(String optionType) throws Exception{
        // Acquire a JMS Connection Factory
        _factory = new ActiveMQConnectionFactory(_brokerURL);
        // Create a JMS Connection
        _connection = _factory.createConnection();
        // Start the JMS connection
        _connection.start();
        // Create a session from the JMS Connection
        _session = _connection.createSession(false, _session.AUTO_ACKNOWLEDGE);
        this._optionType = optionType;


        _stockPathObject = new StockPathObject(optionType);
//        System.out.println(_connection.getMetaData());
    }

    /** Reset Option Type that Client is listening to */
    public void resetOption(String optionType){
        this._optionType = optionType;
    }


    /** Begin the connection */
    @Override
    public void run() {
        try {
            // Acquire JMS Destination Client is listening to
            Destination destination_listening = _session.createQueue(_optionType);
            Destination destination_writing = _session.createTopic(_optionType);
            _producer = _session.createProducer(destination_writing);
            _producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            _consumer = _session.createConsumer(destination_listening);
//        MapMessage mapMessage_response;
            MapMessage mapMessage_response = _session.createMapMessage();
            _consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    MapMessage mapMessage = null;
                    if (message instanceof MapMessage) {
                        try {
                            mapMessage = (MapMessage) message;
                            _stockPathObject.setStockPathObject(mapMessage);
                            double payout = _stockPathObject.getPayout();
//                        System.out.println( payout);
                            message.acknowledge();

                            // Note: While message read back is only payout in our case, message should also
                            // contain information regarding what request it is responding to.
                            mapMessage_response.clearBody();
                            mapMessage_response.setDouble("payout", payout);
                            _producer.send(mapMessage_response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }



    }

}

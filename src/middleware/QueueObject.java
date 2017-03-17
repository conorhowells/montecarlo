package middleware;

import Resources.Option;
import Resources.StatsCollector;
import sun.security.krb5.internal.crypto.Des;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import java.util.HashMap;

/**
 * Created by conorhowells on 11/19/16.
 */
public class QueueObject {
    private Destination _queue;
    public Destination getQueue(){return _queue;}
    private Destination _topic;
    public Destination getTopic(){return _topic;}
    private MessageConsumer _consumer;
    public MessageConsumer getConsumer(){return _consumer;}
    private Option _option;
    public Option getOption(){return _option;}
    private StatsCollector _statsCollector;
    public StatsCollector getStatsCollector(){return _statsCollector;}
    private Boolean _stop;
    public Boolean getStop(){return _stop;}
    public void setStop(Boolean stop){_stop = stop;}
    private String _payOut;
    public String getPayOut(){return _payOut;}


    /** We want to generate queues/topics for all option types we want to calculate
     *
     * @param producer producer object we are creating this object for
     * @param payout a string (Either EuroCall or AsianCall etc.
     * @throws Exception
     */
    public QueueObject(Session session, String payout, Option option) throws Exception{
        _queue = session.createQueue(payout);
        _topic = session.createTopic(payout);
        _consumer = session.createConsumer(_topic);
        _statsCollector = new StatsCollector();
        _stop = new Boolean(true);
        _payOut = payout;
        _option = option;
    }

}

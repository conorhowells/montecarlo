package middleware;

import Interfaces.PayOut;
import Interfaces.StockPath;
import PayOutClasses.AsianCall;
import PayOutClasses.EuroCall;
import Resources.Option;
import StockPathClasses.GeometricBrownianMotion;

import javax.jms.MapMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Object that stores stock path Creation for Client.
 * Created by conorhowells on 11/19/16.
 */
public class StockPathObject {

    private Option _option;
    private StockPath _stockPathGenerator;
    private List<Double> _stockPath;
    private PayOut _payOut;
    private MapMessage _message;

    public StockPathObject(String optionType) throws Exception{
        if (optionType.toString().contains("Euro")){
            if (optionType.toString().contains("Call")){
                _payOut = new EuroCall(0);
            } else{
                throw new Exception("Unknown Euro optionType input");
            }
        } else if (optionType.toString().contains("Asian")){
            if(optionType.toString().contains("Call")){
                _payOut = new AsianCall(0);
            } else{
                throw new Exception("Unknown Asian optionType input");
            }
        } else {
            throw new Exception("Unknown optionType input (Asian/Euro)");
        }

    }

    public void setStockPathObject(MapMessage message) throws Exception{
        _message = message;
        // Obtain fields from message
        Double current = _message.getDouble("current");
        Double interest = _message.getDouble("interest");
        Double volatility = _message.getDouble("volatility");
        Double strike = _message.getDouble("strike");
        Integer numOfDays = _message.getInt("numOfDays");
        PayOut payout = _payOut;
        payout.setStrike(strike);

        _option = new Option.OptionBuilder().setCurrentPrice(current).setInterestRate(interest).setNumOfDays(numOfDays)
                .setVolatility(volatility).setStrikePrice(strike).setPayOut(payout).createOption();
        _payOut = _option.getPayOut();
        _stockPathGenerator = new GeometricBrownianMotion(_option);
        _stockPath = new ArrayList<Double>(_option.getNumOfDays()+1);
    }

    public double getPayout() throws Exception{
        _stockPath.clear();
        _stockPath.addAll(_stockPathGenerator.getSimulatedPath());
        _payOut.setStockPath(_stockPath);
        double payout = _payOut.getPayOut();
        return payout;
    }

}

package PayOutClasses;

import Interfaces.PayOut;

import java.util.List;

/** Calculates the Asian Call Payout (average of path)
 * Created by conorhowells on 10/22/16.
 */
public class AsianCall implements PayOut {
    private List<Double> _stockPath;
    private double _strike;
    private int _stockPathLength = 0;

    public AsianCall(double strike){
        _strike = strike;
    }

    /** Calculate average of Stock path
     *
     * @return double which is average of stock path
     */
    public double averageS(){
        double sum = 0.0;
        for(int i = 0; i<_stockPathLength; i++){
            sum = sum + _stockPath.get(i);
        }
        return sum/_stockPathLength;
    }


    @Override
    /** An asian call payout is equal to the max(average(S(t)) - K, 0)
     * @return double payout
     */
    public double getPayOut(){
        double payoff = averageS();
        if (_strike >= payoff) {
            return 0.0;
        } else {
            return payoff - _strike;
        }
    }


    @Override
    /** Sets the StockPath object
     * @param stockPath list<Double>
     */
    public void setStockPath(List<Double> stockPath) throws Exception{
        if (_stockPathLength != stockPath.size()) {
            if(_stockPathLength != 0) {
                throw new Exception(String.format("Stock Paths are not the same length\nold: " + _stockPathLength + "\nnew: " + stockPath.size()));
            }
        }
        _stockPath = stockPath;
        _stockPathLength = _stockPath.size();

    }

    @Override
    /** Sets the Strike Price
     *
     */
    public void setStrike(double strike){
        _strike = strike;
    }
}

package PayOutClasses;

import Interfaces.PayOut;
import Interfaces.StockPath;

import java.util.List;

/** Calculates the Payout from a path for a European Call option
 * Created by conorhowells on 10/21/16.
 */
public class EuroCall implements PayOut {
    private List<Double> _stockPath;
    private double _strike;
    private int _stockPathLength = 0;

    public EuroCall(double strike){
       // _stockPath = stockPath;
       // _stockPathLength = _stockPath.size();
        _strike = strike;
    }

    @Override
    /** A euro call option is equal to the max(lastDayOfPrice - Strike, 0)
     * @return double payout
     */
    public double getPayOut() {
        if (_strike >= _stockPath.get(_stockPathLength - 1)) {
            return 0.0;
        } else {
            return _stockPath.get(_stockPathLength - 1) - _strike;
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

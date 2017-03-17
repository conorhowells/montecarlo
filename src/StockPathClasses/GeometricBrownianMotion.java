package StockPathClasses;

import Interfaces.RandomVectorGenerator;
import Interfaces.StockPath;

import java.util.ArrayList;
import java.util.List;

import RandomVectorClasses.AntiTheticRandomVectorGenerator;
import RandomVectorClasses.GaussianRandomVectorGenerator;
import Resources.Option;

/** This class uses Random Variables Generated via RandomVectorGenerator
 * to create a random stock path and it's antithetic
 * Created by conorhowells on 10/18/16.
 */
public class GeometricBrownianMotion implements StockPath{

    private RandomVectorGenerator _generator;
    //private List<Double> _prices;
    private Option _option;

    public GeometricBrownianMotion(Option option){
        this._option = option;
       // this._prices = new ArrayList<Double>();
        this._generator = new AntiTheticRandomVectorGenerator(new GaussianRandomVectorGenerator(_option.getNumOfDays()));
    }

    @Override
    /** obtain Generator being used
     * @return RandomVectorGenerator
     */
    public RandomVectorGenerator getGenerator(){
        return _generator;
    }

    @Override
    /** Create a Simulated Stock Path based on geometric Brownian Motion
     *
     */
    public List<Double> getSimulatedPath(){
        //Initialize current price
        List<Double> simulatedPath = new ArrayList<Double>(_option.getNumOfDays()+1);
        simulatedPath.add(_option.getCurrentPrice());
        Double[] randomVector = getGenerator().getVector();
        for(int i = 1; i <= _option.getNumOfDays(); i++){
            double exponential = (_option.getInterestRate()-(_option.getVolatility()*_option.getVolatility()/2)) + (_option.getVolatility()*randomVector[i-1]);
            double price = (simulatedPath.get(i-1))*Math.exp(exponential);
            simulatedPath.add(price);
        }
        return simulatedPath;
    }


}

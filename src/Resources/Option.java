package Resources;

import Interfaces.PayOut;
import PayOutClasses.EuroCall;

/**
 * Created by conorhowells on 10/21/16.
 */
public class Option {


    private double _currentPrice;
    private double _volatility;
    private double _interestRate;
    private int _numOfDays;
    private double _strikePrice;
    private PayOut _payOut;


    public double getCurrentPrice() {
        return _currentPrice;
    }

    public double getVolatility() {
        return _volatility;
    }

    public double getInterestRate() {
        return _interestRate;
    }

    public int getNumOfDays() {
        return _numOfDays;
    }

    public double getStrikePrice() {
        return _strikePrice;
    }

    public PayOut getPayOut(){return _payOut;}

    public Option(
            double currentPrice,
            double strikePrice,
            int numOfDays,
            double volatility,
            double interestRate,
            PayOut payOut
    ) {
        this._currentPrice = currentPrice;
        this._strikePrice = strikePrice;
        this._numOfDays = numOfDays;
        this._volatility = volatility;
        this._interestRate = interestRate;
        this._payOut = payOut;
    }

    public static class OptionBuilder

    {
        private double _currentPriceBuild = 0.0;
        private double _volatilityBuild = 0.01;
        private double _interestRateBuild = 0.0001;
        private int _numOfDaysBuild = 252;
        private double _strikePriceBuild = 0.0;
        private PayOut _payOutBuild = new EuroCall(_strikePriceBuild);

        public OptionBuilder() {
        }

        public OptionBuilder setCurrentPrice(double currentPrice) {
            this._currentPriceBuild = currentPrice;
            return this;
        }

        public OptionBuilder setVolatility(double volatility) {
            this._volatilityBuild = volatility;
            return this;
        }

        public OptionBuilder setInterestRate(double interestRate) {
            this._interestRateBuild = interestRate;
            return this;
        }

        public OptionBuilder setNumOfDays(int numOfDays) {
            this._numOfDaysBuild = numOfDays;
            return this;
        }

        public OptionBuilder setStrikePrice(double strikePrice) {
            this._strikePriceBuild = strikePrice;
            return this;
        }

        public OptionBuilder setPayOut(PayOut payOut){
            this._payOutBuild = payOut;
            return this;
        }

        public Option createOption() throws Exception{
            if (_numOfDaysBuild < 1){throw new Exception(String.format("Number of days must be greater than 0"));}
            return new Option(_currentPriceBuild, _strikePriceBuild, _numOfDaysBuild, _volatilityBuild, _interestRateBuild, _payOutBuild);
        }
    }
}



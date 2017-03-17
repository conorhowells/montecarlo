package Resources;

/**
 * Created by conorhowells on 10/21/16.
 */
public class StatsCollector {
    private double _mean;
    private int _numOfDoubles;
    private double _numOfDoublesD;
    private double _stdDev;
    private double _meanSq;

    public StatsCollector(){
        this._mean = 0.0;
        this._numOfDoubles = 0;
        this._numOfDoublesD = 0.0;
        this._stdDev = 0.0;
        this._meanSq = 0.0;
    }

    public void reset(){
        this._mean = 0.0;
        this._numOfDoubles = 0;
        this._numOfDoublesD = 0.0;
        this._stdDev = 0.0;
        this._meanSq = 0.0;
    }
    public double getMean(){return _mean;}
    public double getNumOfDoubles(){return _numOfDoublesD;}
    public double getStdDev(){return _stdDev;}
    public void addDouble(double newDouble){
        _numOfDoubles = _numOfDoubles + 1;
        _numOfDoublesD = _numOfDoubles;
        double newMean = (((_numOfDoublesD-1)/_numOfDoublesD)*_mean) + (1/_numOfDoublesD)*newDouble;
        _meanSq =(_numOfDoublesD-1.0)/_numOfDoublesD*_meanSq + newDouble*newDouble/_numOfDoublesD;
        _stdDev = Math.sqrt(_meanSq-newMean*newMean);

        // Using Welford's method for standard deviation (xN−x¯N)(xN–x¯N−1)
        //_stdDev = Math.sqrt((newDouble - newMean)*(newDouble - _mean));
        _mean = newMean;
    }
}

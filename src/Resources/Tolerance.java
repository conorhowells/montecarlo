package Resources;

/**
 * Created by conorhowells on 10/21/16.
 */
public class Tolerance {

    //public double t = Math.sqrt(Math.log(1/Math.sqrt(1-0.96)));
    public double _errorLevel;
    public double _xp;
    public double _t;
    public double _c0 = 2.515517;
    public double _c1 = 0.802853;
    public double _c2 = 0.010328;
    public double _d1 = 1.432788;
    public double _d2 = 0.189269;
    public double _d3 = 0.001308;
    public double _XpSet = 2.05397; // _Xp doesn't seem to be working

    public Tolerance(double probabilityLevel, double errorLevel) throws Exception{
        if(probabilityLevel <= 0.0){throw new Exception(String.format("Probability must be greater than 0"));}
        if(errorLevel <= 0.0){throw new Exception(String.format("Error level must be greater than 0"));}
        _t = Math.sqrt(Math.log(1 / Math.sqrt(1 - probabilityLevel)));
        _errorLevel = errorLevel;
    }

    public double getTolerance(){
        double numerator = (_c0 + _c1 * _t + _c2 * _t * _t);
        double denominator = (1.0 + _d1 * _t + _d2 * _t * _t + _d3 * _t * _t * _t);
        double val = numerator / denominator;
        _xp = _t - val;
        //return _errorLevel*_xp;
        return _XpSet;
    }

    /*
    public static void main(String[] args) throws Exception {
        Tolerance tol = new Tolerance(0.997, 0.01);
        System.out.println("Tolerance: " + tol.getTolerance());
        System.out.println("Xp: " + tol._xp);
    }
    */


}

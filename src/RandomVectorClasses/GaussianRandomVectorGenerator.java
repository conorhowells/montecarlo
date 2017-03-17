package RandomVectorClasses;

import Interfaces.RandomVectorGenerator;
import java.util.Random;
/** Create a Gaussian distributed random number
 * Using Eran's Encapsulation example as template
 * from Lecture 2.
 * Created by conorhowells on 10/18/16.
 */
public class GaussianRandomVectorGenerator implements RandomVectorGenerator {

    // Data
    /**
     * object that generates random gaussian numbers
     */
    private Random _generator;

    /**
     * length of vector to generate
     */
    private int _vectorLength;

    /**
     * vector that output is stored in
     */
    private Double[] _gaussianRandomVector;

    //CTOR

    /**
     *
     * @param vectorLength Length of randomized vector desired.
     */
    public GaussianRandomVectorGenerator(int vectorLength){
        _generator = new Random();
        _vectorLength = vectorLength;
        // _gaussianRandomVector = new Double[vectorLength];
    }

    //Methods

    @Override
    /** Creates a vector of random numbers from Random.nextGaussian()
     * @return Double[] of randomized vectors with length equal to the vectorlength inputted into the class
     */
    public Double[] getVector(){
        _gaussianRandomVector = new Double[_vectorLength];
        for(int i = 0; i<_vectorLength; i++){
            _gaussianRandomVector[i] = _generator.nextGaussian();
        }
        return _gaussianRandomVector;
    }

    /** Set */
}

package RandomVectorClasses;

import Interfaces.RandomVectorGenerator;

import java.util.Random;

/**
 * Created by conorhowells on 10/18/16.
 */
public class AntiTheticRandomVectorGenerator implements RandomVectorGenerator {

    /**
     * Storing Generated Vector
     */
    private RandomVectorGenerator _generator;

    /**
     * Marking process as finished
     */
    private boolean _complete = false;

    /** AntiThetic Vector
     */
    private Double[] _antiTheticVector;

    public AntiTheticRandomVectorGenerator(RandomVectorGenerator generator){
        this._generator = generator;
    }

    @Override
    /** Generate Vector if it doesn't exist or create opposite
     * @return the randomly generated vector
     */
    public Double[] getVector(){
        if(_complete) {
            // When Original has been generated, created opposite
            for (int i = 0; i < _antiTheticVector.length; i++) {
                _antiTheticVector[i] = _antiTheticVector[i] * -1;
            }
            _complete = false;
        }else{
            //When original has not been generated, created original
                _antiTheticVector = _generator.getVector();
                _complete = true;
            }

        return this._antiTheticVector;


    }


}

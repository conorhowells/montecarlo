package Interfaces;

/**
 * Created by conorhowells on 10/17/16.
 */
import java.util.List;
import StockPathClasses.*;
public interface StockPath {

    List<Double> getSimulatedPath();
    RandomVectorGenerator getGenerator();
}

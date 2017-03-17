package Interfaces;

/**
 * Created by conorhowells on 10/17/16.
 */
import java.util.List;

/** Payout options are
 * Call Option (S(T)-K)+
 * Lookback Call (SMAX-K)+
 * LookBack Put (K-SMIN)+
 * Asian Call (Avg(S)-K)+
 */
public interface PayOut {


    /** Takes Stock path and returns the payout for the option
     *
     * @param path needs the whole path to price certain options
     * @return returns the options payout
     */
    double getPayOut();

    /** Inputs a Stock path into the PayOut object
     *
     */
    void setStockPath(List<Double> stockPath) throws Exception;

    /** Set/Reset Strike Price
     *
     */
    void setStrike(double strike);
}

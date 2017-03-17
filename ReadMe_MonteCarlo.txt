MonteCarlo Assignment ReadMe File
Conor Howells
N10617611

Introduction:

The zip file should contain this readme as well as an src and a Junit folder. The set-up of the project (src folder) is described below as well as where uses of Mockito can be found in the testing (Junit folder). The required simulations are run and described in Main.java. Main.java will out put statistics and the option prices for each test to the Terminal. The code that prints these statistics can be found in the simulate method of the Simulation.class. The sections are as follows: Answers, Classes, Testing

Answers:
Problem 1 (Euro Call): Option price ~6.35. Standard Deviation ~13. Iterations: ~70,000

Problem 2 (Asian Call): Option price <~2.25. Standard Deviation ~5.7. Iterations: ~13,619.

Classes:

The following section provides emphasis on the classes that are highlighted on the homework.

The main classes are:
Main.class - runs the problems as desribed in the homework set.

Simulation.class - Initializes a Antithetic path generator, a payout calcuator and a stat collector to run the simulation (simulate method) and and decide when to stop based on a desired tolerance level.


Interfaces package: Holds the three interfaces required for the project: RandomVectorGenerator.class, PayOut.class, StockPath.class. Each class has a related package where package name is defined by {Interface}Classes.

RandomVectorClasses.AntitheticRandomVectorGenerator.class. Uses an AntiThetic decorator pattern to create a vector of a defined length along with the same vector multiplied by -1. RandomVectorClasses.GaussianRandomVector.class creates the Gaussian generated random vectors.

PayOutClasses package - holds the two payout functions that generates an option payout from a specific stock path.

StockPathClasses package - Used to output simulated stock prices. The model for doing so in the package is a Geometric Brownian Motion.

Resources.Option - creates an Option object that holds all relevant information for a given option including current price, strike price, Payout type, etc. An Option builder pattern is used for the construction.

Resources.StatCollector - collects relevant statistics based the simulations. Statistics include the Option Price, number of iterations and the standard deviation of the option price that is simulated.

Resources.Tolerance - Calculates the tolerance level based on the probability level and estimation error level desired.

Testing:

The following section provides an overview of the testing done in this project. While Junit tests were carried out for all classes. Mockito was used in particular tests where randomized stock prices had to be set specific numbers so that other aspects of the classes could be tested. In particular, Mockito was used in the test_Simulation.class and the Test_AntiTheticRandomVectorGenerator.class.

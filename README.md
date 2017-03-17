# montecarlo
Monte Carlo option pricing in Java


The classes written for this assignment are stored in the 'Middleware' package. The other packages are taken from the 'MONTECARLO' assignment. I have included the readme.txt file "ReadMe_montecarlo.txt" for that assignment in case you are interested in the formation of those packages.

The pricing for the two options requested is done within the Main.java class. Simply run the main method in the class to simulate the objects using the ActiveMQ middleware. The answers will be outputted to the Run Terminal. The Java Thread class is used so that the clients and server can both be run from the main class. The other written classes are described below. To note, JMSPublisher.java is the server object while JMSClient.java is the client object.

The set-up is as follows:

JMSPublisher.java - Creates the ActiveMQ server. After initiating the class, the user can set options for a given type of instrument. In our case, these two options are a EuroCall and an AsianCall. When the option is set, the JMSPublisher creates two unique destinations for a single Option: a queue which is publishes to so that the clients can read and a topic which it reads the clients responses from. This set-up was chosen for a few reasons.

A queue is the best choice in the case of simulation requests because the server must be in control the number of responses it obtains. A PTP queue thus allows for multiple clients to read a queue, but not each request and allows only one response for each simulation request.

The server consumes from a topic because, although it is not done within this assignment, the use of a topic for client responses allows a history of prices to be obtained. Thus, if a server wanted to say run multiple euroCall simulations with different levels or error and tolerance, this could be done fairly easily and all new simulations wouldn't have to be created which would be slower. This could also be done using a queue, but in that case, the server would have to store all of the queue values it obtains which would be inefficient in terms of space.

Similarly, the use of one topic and one queue is simple for naming conventions i.e. the names of the topic and queue for a given option can be the same rather than two names which would have to be used if two queues or topics were used (ex: createQueue("EuroCall") and createTopic("EuroCall") vs. createQueue("EuroCall") and createQueue("EuroCall_1") etc.).

Each option that is simulated has two of it's own channels for speed purposes. While the upfront cost is slightly higher to run multiple options in a simulation (two clients are necessary instead of one), the client does not have to read the type of payout it must calculate for each message (i.e. EuroCall vs. AsianCall) which can make a big difference in terms of speed when the number of stock path simulations gets large. Similarly, it allows for more optionality if say the server wanted to simulate an AsianCall and EuroCall, but wanted a different stockPathGenerator (i.e. Geometric brownian motion etc.) to be used for each client. If one client was used, the type of StockPathGenerator would have to be desribed in each object, and again, the client class would have to read this from each message uniquely which would take more time.

The JMSPublisher reads each response and determines, using a StatCollector, if after 100 simulation responses it should request another 100 simulations from the clients.

MapMessage is used for the messaging services. This message type was chosen due to the ability to send multiple types of information simply. Similarly, while not the case for this assignment, the use of MapMessage would also allow the client to send back extra information if it wanted to provide extra details about the option that it priced i.e. pricing method etc.


JMSClient.java - is a simple ActiveMQ client that uses a messageListener to listen to simulation requests on the queue and if a simulation is requested, it responds by writing the payout it calculates to the topic it is assigned to publish to.

To note, the client creates the proper payout based on the name of the channel is consumes from i.e. it uses the words Euro/Asian and Call to instantiate the proper payout object. This is done within the StockPathObject.java class. The set-up was done for two reasons. The first is that it allows for other payout structures to be added to the class quite simply i.e. if puts or different types of calls were desired. Secondly, it allows for multiple types of the same call to be simulated within a single simulation i.e. if a user wanted to price two EuroCalls but with different Strikes, they could easily do so by creating two options and denoting them both with names that include "Euro" and "Call" such as "EuroCall" and "EuroCall1". The creation of clients that subscribe to these channels would instantiate the proper payout methods on instantiation since both names contain "Euro" and "Call".



Objects (simple, storage based classes for ease of use):
StockPathObject.java - This is a simple object that stores the stockpath and payout calculator. It is called by JMSClient.java (The activeMQ class). It was split from the client object because in this way, so that a different stockpath generator and payout calculator could be implemented easily if desired. Also so the JMSClient.java could be implemented for other purposes other than stockpath generation.

QueueObject.java - This is a simple object used by JMSPublisher.java that stores and creates the necessary objects associated with a given option.

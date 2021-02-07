# Tools
* Download and install the Java Development Kit 11. You can download it from: [here][1]
* Download and install Maven 3. You can download it from: [here][2]
* Using Lombok, so it is recommended to add a Lombok plugin to your IDE, but not mandatory.

# Build
* Build the application with the following command in the root directory of project: **mvn clean install**
* You can build the application without tests too with the following command in the root directory of project: **mvn clean install -DskipTests**


# Start the application
* You can reach the application by the following url: **localhost:8080** or by the following Ip-address **127.0.0.1**  [example][3]
* If no rates provided via the post post endpoint then the get endpoint will always return NOT FOUND response

# Assumptions
* I was not sure about how the system will receive the customer rates. So I have created a separate endpoint
* I was not sure if the system will receive more GET booking estimates requests OR more POST requests to persist customer rates. So, my assumptions are as below:
    * Persists the rates as it receives at POST rates request without any extra processing (as currently implemented)
        * Always does extra processing on rates at each GET estimates request
    * Second option can be:
        * Organise/sort the rates on each POST customer rates request
        * Just calculates the estimates from the organised rates for each GET estimates request
* Keeping the rates as list in the service is not a good practice and thread unsafe but in real project we should be loading it from some persisted place. So keeping it in service for the assignment


[1]: https://www.oracle.com/technetwork/java/javase/downloads/5066655
[2]: https://maven.apache.org/download.cgi
[docker-download]: https://docs.docker.com/docker-for-windows/install/
[3]: http://localhost:8080
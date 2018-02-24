# PaysafeDevAPIMonitor
This is a Maven-Spring boot Rest API application which monitors the state of the open Developer API server of Paysafe.com

# Setup
This application has been developed in Spring STS. Once cloned, should be seamlessly imported in the STS IDE.
It the POM file includes the necessary configurations for setup.

# Deployment
- Run "mvn clean package" and "mvn install" to run the given tests and a jar will be created.
- Locate and run the jar.

# Key ReST Endpoints
- The target server whose status has to be monitored has been referred as service/server interchangeably 
in the application and it's documentation
- I have used Postman application to test the services.
1. ReST endpoint of type POST, to intialize the monitoring of a particular service/server
http://localhost:8091/api/monitoring/start
- Sample Input:
{
	"serviceURL":"https://api.test.paysafe.com/accountmanagement/monitor",
	"interval":2
}

2.  ReST endpoint of type GET, to query monitoring log of a particular service/server.
- Sample Input: 
http://localhost:8091/api/monitoring/servicelog?serviceurl=https://api.test.paysafe.com/accountmanagement/monitor

3. ReST endpoint of type GET, to stop monitoring a particular service/server.
- Sample Input: 
http://localhost:8091/api/monitoring/stop?serviceurl=https://api.test.paysafe.com/accountmanagement/monitor

4. By default the application runs on port 8091. Configuration can be changed in application.properties in src/main/resources
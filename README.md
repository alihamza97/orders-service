# orders-service

This app creates the orders and store them in the in-memory database. 

### prerequisite

Please make sure following tools are installed in your local machine to run this project.

-JDK11

-Maven

-Docker

### Instructions

Please follow the instructions below to run this app.

1:- Download this project.

2:- Open windows command line.

3:- Hit mvn clean install in the cmd to build the project.

4:- Type the following commmand to build docker image: ( Optional : Hit mvn spring-boot:run  to run the spring-boot app if docker is not installed in your local machine)

        docker image build -t orders-service .
        
5:- The above command will create a docker image with name orders-service. To run the docker type the following command:

        docker container run --name orders-service -p 8080:8080 -d orders-service

6:- Type the followng command to check docker logs in the cmd followed by the 2 initial charachters/numbers of the docker container ID

        docker container logs -f ID (e.g. docker container logs -f 23)
        
7: Go to the following url once the app is and running to check the api specs

      http://server:port/swagger-ui.html ->   http://localhost:8080/swagger-ui.html
      
8:- Downloaded project contains postman collections which can be imported into postman to make post/get requests.

POST Request sample 

{ "email": "janet.weaver@reqres.in", "firstName": "janet", "lastName": "weaver", "productID":134 }



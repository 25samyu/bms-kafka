# bms-kafka
## Bank Management System project for Kafka microcredential  

### Access URLs
#### Postman URLs:
 
###### POST:
   http://localhost:8000/portal-service/register  
   http://localhost:8000/portal-service/login  
   http://localhost:8000/portal-service/update  
   http://localhost:8000/portal-service/apply  


###### GET:
   http://localhost:8000/portal-service/getRegistrationForm  
   http://localhost:8000/portal-service/getLogin  
   http://localhost:8000/portal-service/home  
   http://localhost:8000/portal-service/getUpdateForm  
   http://localhost:8000/portal-service/getLoanApplication  
   http://localhost:8000/portal-service/retrieve  
   http://localhost:8000/portal-service/logout  

#### Swagger Api URLs:

###### Customer Service:  
http://localhost:7071/swagger-ui.html
###### Loan Service:  
http://localhost:7072/swagger-ui.html
###### Authorization Service:  
http://localhost:7073/swagger-ui.html

### Order Of Starting Applications
  Start Zookeeper  
  Start Kafka  
  ###### Start the microservices in the following order:  
  EurekaDiscoveryServerApplication  
  BmsPortalController  
  CustomerApplication  
  LoanApplication  
  AuthorizationApplication  
  ZuulGatewayApplication  
  
  ### Testing Report
  Customer Service - 95.9%  
  Loan Service - 96.7%  
  Authorization Service - 90.0%  

# Loblaws Ecommerce Training
## _Delivery Service_

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://pscode.lioncloud.net/ecommerce-training-ground/delivery-service/-/pipelines)

Delivery service is responsible for creating shipments for fulfilled orders and track the orders with various status codes until it is delivered to the end customer by a delivery service provided. Mock Delivery Service provider is embedded in this code only.
Order Service publishes events to *order-fulfillment* topic
order-fulfillment is subscribed by *deliver-order-fulfill*
Delivery provider will use *shipment-status* topic to provide shipment status
*shipment-status*  is subscribed by *delivery-status-update*


## Features

- Listens to a GCP pubsub topic for fulfilled orders
- Automatically generates shipment for order
- Auto updates order status based on the status provided by delivery service provider
- Deployed to Google Kubernetes (GKE) cluster for scalability and availability


## Running Locally

```sh
cd delivery-service
mvn clean package -Denv=test
java -jar -Denv=test target/delivery-service-0.0.1-SNAPSHOT.jar 
```
## Running Test cases locally

```sh
cd delivery-service
mvn test -Denv=test 
```

## License

MIT


# Wishlist Microservice API

This microservice is responsible for managing an e-commerce Wishlist. It is built using Java 17, Spring Boot, Gradle and MongoDB, and is designed to be deployed using Kubernetes.

This project uses another microservices to query product information and check for existing customers. [The Project Wishlist Support API](https://github.com/raytotti/wishlist-support) you will find an example microservices that provides the end-points with the necessary information for the correct functioning of this project.

## Requirements

To build and run this microservice, you will need the following:

* [IntelliJ IDEA: For project development](https://www.jetbrains.com/pt-br/idea/download/)
* [JDK 17: Required to run the Java project](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Gradle: Required to build the Java project](https://gradle.org/)
* [MongoDB: Required to Database the project](https://www.mongodb.com/)
* [Docker: Required for deploy tho project](https://www.docker.com/)
* [Kubernetes: Required for deploy tho project](https://kubernetes.io/)

## Getting Started
To build and run the microservice, you can choose one of the following options:

* [IDE](#with-ide)
* [Docker](#with-docker)
* [Kubernetes](#with-kubernetes)

### With IDE

To run the project, you will need to following these steps:

1. Clone the repository to your local machine.
2. Configure the MongoDB connection and external API for products and clients in **application.yml** or the environment variables **(VM Options)**.
    ```
    -DMONGO_HOST='<Mongodb url connection>'
    -DMONGO_DB='<Data base name>'
    -DMONGO_USERNAME='<User name>'
    -DMONGO_PASSWORD='<Password>' 
    -DAPI_CLIENT_URL='<External API client URL>' 
    -DAPI_CLIENT_GET_EXISTS='<Path service get exists client>' 
    -DAPI_PRODUCT_URL='<External API product URL>'
    -DAPI_PRODUCT_GET_PRODUCT='<Path service get product information>'
   ```
3. Build the microservice **WishlistApplication** using Gradle: **` gradle build `**.

### With Docker

To run the project with docker, has an image hosted on the Docker Hub: 
https://hub.docker.com/repository/docker/raytottifa/wishlist-api

```shell
docker pull raytottifa/wishlist-api:latest
docker run -p 8080:8080 
    -e MONGO_HOST='<Mongodb url connection>' 
    -e MONGO_DB='<Data base name>' 
    -e MONGO_USERNAME='<User name>' 
    -e MONGO_PASSWORD='<Password>'  
    -e API_CLIENT_URL='<External API client URL>' 
    -e API_CLIENT_GET_EXISTS='<Path service get exists client>' 
    -e API_PRODUCT_URL='<External API product URL>'
    -e API_PRODUCT_GET_PRODUCT='<Path service get product information>'
    -e PORT=8080 
    wishlist-api
```

### With Kubernetes

Deploy in your Kubernetes cluster following these steps:

1. Apply the ConfigMap Mongo located at `./deploy/kubernetes`
    ```shell
     kubectl apply -f ./deploy/kubernetes/mongo-wishlist-config.yml
   ```
2. Apply the Secret Mongo located at `./deploy/kubernetes`
    ```shell
     kubectl apply -f ./deploy/kubernetes/mongo-wishlist-secret.yml
   ```
3. Apply the Deployment Mongo located at `./deploy/kubernetes`
    ```shell
   kubectl apply -f ./deploy/kubernetes/mongo-wishlist-deployment.yml
   ```
4. Apply the ConfigMap Api located at `./deploy/kubernetes`
    ```shell
   kubectl apply -f ./deploy/kubernetes/wishlist-config.yml
   ```
5. Apply the Deployment Api located at `./deploy/kubernetes`
   ```shell
   kubectl apply -f ./deploy/kubernetes/deployment.yml
   ```
   
## Endpoints
The following endpoints are available in the microservice:

* **POST /api/v1/wishlists/clients/{clientId}/products** - Add a new item to the list of product items for the informed clientId.
```shell
curl --location --request POST 'http://<applicationUrl>:<applicationPort>/api/v1/wishlists/clients/<clientId>/products' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productId":"<productId>"
}'
```
* **DELETE /api/v1/wishlists/clients/{clientId}/products/{productId}** - Remove an item from the list of product items for the informed clientId.
```shell
curl --location --request DELETE 'http://<applicationUrl>:<applicationPort>/api/v1/wishlists/clients/<clientId>/products/<productId>'
```
* **GET /api/v1/wishlists/clients/{clientId}** - Retrieve the list of favorite items for the informed clientId.
```shell
curl --location --request GET 'http://<applicationUrl>:<applicationPort>/api/v1/wishlists/clients/<clientId>'
```
* **GET /api/v1/wishlists/clients/{clientId}/products/{productId}/exists** - Checks if the informed product belongs to the list of product items for the informed clientId.
```shell
curl --location --request GET 'http://<applicationUrl>:<applicationPort>/api/v1/wishlists/clients/<clientId>/products/<productId>/exists'
```

## Conclusion
This microservice provides a simple and scalable solution for managing an e-commerce Wishlist. By leveraging Java 17, Spring Boot, Gradle, and MongoDB, and deploying with Kubernetes, this microservice is well-equipped to handle large volumes of traffic and provide a seamless user experience.

## TODO
* As a future implementation, we are going to implement the communication of this microservice with a messaging structure. with the intention of ensuring the integrity of product and customer data.

# Checkout Component 3.0 #

### Architecture
Domain-centric with two supporting layers. The infrastructure layer provides access to database and state storage. The endpoint layer exposes domain logic through REST API.

### Running tests
```
mvn test
```

### Running application
```
mvn package
java -jar target/checkout-component-1.0-SNAPSHOT.jar --spring.config.location=application.properties
```

##### Application configuration
Application could be run in two modes:
1. with in-memory H2 database (creates schema using schema.sql and loads data from data.sql)
2. with external PostgreSQL database

Example configuration for first mode:
```
server.contextPath=/shopping

spring.datasource.initialize=true

shoppingCart.cache.expireAfterAccessMinutes=30
shoppingCart.cache.maximumSize=10000
```

Example configuration for second mode:
```
server.contextPath=/shopping

spring.datasource.url=jdbc:postgresql://localhost:5432/checkout-component
spring.datasource.username=postgres
spring.datasource.password=********
spring.datasource.initialize=false

shoppingCart.cache.expireAfterAccessMinutes=30
shoppingCart.cache.maximumSize=10000
```

### REST API
1. Creates new shopping cart. Returns cart ID.
```
POST /shopping/carts
```
2. Adds {quantity} items of type {productId} to shopping cart with {cartId}
```
POST /shopping/carts/{cartId}/items/{productId}/{quantity}
```
3. Removes {quantity} items of type {productId} from shopping cart with {cartId}
```
DELETE /shopping/carts/{cartId}/items/{productId}/{quantity}
```
4. Retrieves content of shopping cart with {cartId}
```
GET /shopping/carts/{cartId}
```
5. Generates receipt for shopping cart with {cartId}
```
GET /shopping/checkouts/{cartId}
```
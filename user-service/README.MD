# User-Management Module

## Application Properties

```
## default connection pool
spring.datasource.hikari.connectionTimeout=20000
spring.datasource.hikari.maximumPoolSize=5

## PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/e-wallet
spring.datasource.username=postgres
spring.datasource.password=020416

#drop n create table again, good for testing, comment this in production
#pring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation= true
#spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.PostgreSQLDialect
server.port=8081
server.ssl.enabled=true
server.ssl.key-store= user-management/src/main/resources/bootsecurity.p12
server.ssl.key-store-password= 020416
server.ssl.keyStoreType= PKCS12
server.ssl.keyAlias= bootsecurity
ph1eu.appprop.jwtCookieName= PhamMinhHieu
ph1eu.appprop.jwtSecret= PhamMinhHieu16042002SecretKeyPhamMinhHieu16042002SecretKeyPhamMinhHieu16042002SecretKey
ph1eu.appprop.jwtExpirationMs= 86400000
ph1eu.appprop.signupkey=PhamMinhHieu1604
```


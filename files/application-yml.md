## application.yml

```java
spring:
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

  profiles:
    include:
      - aws

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: "jdbc:mysql://URL:3306/post_mission?userSSL=false&useUnicode=true&serverTimezone=Asia/Seoul"
    username: USERNAME
    password: PASSWORD
    hikari:
      auto-commit: false
      connection-test-query: SELECT 1
      minimum-idle: 10
      maximum-pool-size: 50
      transaction-isolation: TRANSACTION_READ_UNCOMMITTED

  mail:
    host: smtp.gmail.com
    port: 587
    username: EMAIL
    password: PASSWORD
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
          timeout: 5000

  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        #        show_sql: true
        format_sql: true
  jackson:
    property-naming-strategy : SNAKE_CASE

server:
  port : 8080

logging:
  level:
    org.hibernate.SQL: debug
#    org.hibernate:type: trace
```
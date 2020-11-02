# Sample application of Spring Web MVC

## Purpose

To confirm implementation of each function of microservices. 

## Function

* Server side rendering with Thymeleaf.
* API client with Retrofit2 and Okhttp3.
* Application Metrics with spring boot actuator.
  * Can see metrics from http://localhost:8080/actuator/prometheus 
* Tracing with zipkin and brave.
  * Build zipkin server from https://github.com/openzipkin-attic/docker-zipkin
* CircuitBreak with spring-cloud resilience4j.

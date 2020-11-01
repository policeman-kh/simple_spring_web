package sandbox.simple_spring_web;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimpleSpringWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSpringWebApplication.class, args);
    }

    // To use h2c.
    @Bean
    public TomcatConnectorCustomizer connectorCustomizer() {
        return (connector) -> connector.addUpgradeProtocol(new Http2Protocol());
    }
}

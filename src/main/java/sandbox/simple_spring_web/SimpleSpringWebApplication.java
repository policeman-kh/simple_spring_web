package sandbox.simple_spring_web;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import brave.http.HttpTracing;
import brave.okhttp3.TracingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sandbox.simple_spring_web.client.ZipAddressClient;

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

    @Bean
    OkHttpClient okHttpClient(HttpTracing httpTracing) {
        return new OkHttpClient.Builder()
                .addInterceptor(TracingInterceptor.create(httpTracing))
                .build();
    }

    @Bean
    ZipAddressClient zipAddressClient(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://zipcloud.ibsnet.co.jp")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build()
                .create(ZipAddressClient.class);
    }
}

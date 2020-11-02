package sandbox.simple_spring_web;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import brave.CurrentSpanCustomizer;
import brave.SpanCustomizer;
import brave.Tracing;
import brave.baggage.BaggagePropagation;
import brave.context.slf4j.MDCScopeDecorator;
import brave.http.HttpTracing;
import brave.propagation.B3Propagation;
import brave.propagation.CurrentTraceContext;
import brave.propagation.CurrentTraceContext.ScopeDecorator;
import brave.propagation.Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.servlet.TracingFilter;
import brave.spring.webmvc.SpanCustomizingAsyncHandlerInterceptor;
import zipkin2.reporter.Sender;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * Zipkin tracing configuration.
 * @see <a href="https://github.com/openzipkin/brave-example/tree/master/webmvc4-boot">brave-example</a>
 */
@Configuration
public class TracingConfiguration {
    @Bean
    ScopeDecorator correlationScopeDecorator() {
        return MDCScopeDecorator.newBuilder()
                                .build();
    }

    @Bean
    CurrentTraceContext currentTraceContext(ScopeDecorator correlationScopeDecorator) {
        return ThreadLocalCurrentTraceContext.newBuilder()
                                             .addScopeDecorator(correlationScopeDecorator)
                                             .build();
    }

    @Bean
    Propagation.Factory propagationFactory() {
        return BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY).build();
    }

    @Bean
    Sender sender(@Value("${brave.zipkin.baseUrl:http://127.0.0.1:9411}/api/v2/spans") String zipkinEndpoint) {
        return OkHttpSender.create(zipkinEndpoint);
    }

    @Bean
    AsyncZipkinSpanHandler zipkinSpanHandler(Sender sender) {
        return AsyncZipkinSpanHandler.create(sender);
    }

    @Bean
    Tracing tracing(@Value("${brave.zipkin.name}") String serviceName,
                    Propagation.Factory propagationFactory,
                    CurrentTraceContext currentTraceContext,
                    AsyncZipkinSpanHandler zipkinSpanHandler) {
        return Tracing.newBuilder()
                      .localServiceName(serviceName)
                      .supportsJoin(true)
                      .traceId128Bit(false)
                      .propagationFactory(propagationFactory)
                      .currentTraceContext(currentTraceContext)
                      .addSpanHandler(zipkinSpanHandler)
                      .build();
    }

    @Bean
    SpanCustomizer spanCustomizer(Tracing tracing) {
        return CurrentSpanCustomizer.create(tracing);
    }

    @Bean
    HttpTracing httpTracing(Tracing tracing) {
        return HttpTracing.create(tracing);
    }

    @Bean
    Filter tracingFilter(HttpTracing httpTracing) {
        return TracingFilter.create(httpTracing);
    }

    @Bean
    WebMvcConfigurer tracingWebMvcConfigurer(SpanCustomizingAsyncHandlerInterceptor webMvcTracingCustomizer) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(webMvcTracingCustomizer);
            }
        };
    }
}

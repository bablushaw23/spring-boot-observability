package com.example.observability.order;

import io.opentelemetry.api.trace.Span;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors(new TracingRestTemplateInterceptor()).build();
    }

   class TracingRestTemplateInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            Span currentSpan = Span.current();
            if (currentSpan != null) {
                // Add request headers
                request.getHeaders().forEach((key, value) ->
                        currentSpan.setAttribute("http.request.header." + key, String.join(",", value))
                );
                // Add request body
                currentSpan.setAttribute("http.request.body", new String(body));
            }

            ClientHttpResponse response = execution.execute(request, body);

            if (currentSpan != null) {
                // Add response headers
                response.getHeaders().forEach((key, value) ->
                        currentSpan.setAttribute("http.response.header." + key, String.join(",", value))
                );
            }

            return response;
        }
    }

}

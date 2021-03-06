package gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class AplicationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplicationApiApplication.class, args);
	}
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		String httpUri = uriConfiguration.getHttpbin();
		return builder.routes()
			//.route(p -> p.path("/get").filters(f -> f.addRequestHeader("Hello", "World")).uri(httpUri)) Exemple
			.route(r -> r.path("/museums").uri("https://musea-api.herokuapp.com/")
	            )	
			.route("museu_id",
	                    route -> route
	                            .path("/museums/**")
	                            .filters(f -> f.rewritePath("/museums/(?<RID>.*)", "/museums/${RID}"))
	                            .uri("https://musea-api.herokuapp.com/")
	            )
			.build();
	}
	// end::route-locator[]

	// tag::fallback[]
	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
	// end::fallback[]
}

// tag::uri-configuration[]
@ConfigurationProperties
class UriConfiguration {
	
	private String httpbin = "http://httpbin.org:80";

	public String getHttpbin() {
		return httpbin;
	}

	public void setHttpbin(String httpbin) {
		this.httpbin = httpbin;
	}

}

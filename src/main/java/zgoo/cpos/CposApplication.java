package zgoo.cpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import zgoo.cpos.common.properties.EtcProperties;

// @SpringBootApplication(exclude = { KafkaAutoConfiguration.class })
@SpringBootApplication
@EnableConfigurationProperties({ EtcProperties.class })
public class CposApplication extends SpringBootServletInitializer {

	public CposApplication() {
		super();
		setRegisterErrorPageFilter(false);
	}

	public static void main(String[] args) {
		SpringApplication.run(CposApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CposApplication.class);
	}
}

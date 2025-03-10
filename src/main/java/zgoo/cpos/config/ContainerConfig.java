package zgoo.cpos.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainerConfig {
    @Value("${fwaccess.properties.tomcat_ajp_port}")
    int ajpPort;

    @Value("${fwaccess.properties.tomcat_ajp_protocol}")
    String ajpProtocol;

    @Value("${fwaccess.properties.tomcat_ajp_enable}")
    boolean tomcatAjpEnabled;

    @Value("${fwaccess.properties.tomcat_ajp_address}")
    String ajpAddress;

    @Value("${fwaccess.properties.tomcat_ajp_allowed_request_attributes_pattern:.*}")
    String allowedRequestAttributesPattern;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        if (tomcatAjpEnabled) {
            tomcat.addAdditionalTomcatConnectors(createAjpConnector());
            System.out.println("AJP Connector enabled on port: " + ajpPort);
        }

        return tomcat;
    }

    private Connector createAjpConnector() {
        Connector ajpConnector = new Connector(ajpProtocol);
        ajpConnector.setPort(ajpPort);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setScheme("http");
        ajpConnector.setProperty("address", ajpAddress);
        ajpConnector.setProperty("allowedRequestAttributesPattern", allowedRequestAttributesPattern);

        // 중요: secretRequired를 false로 설정
        AbstractAjpProtocol<?> protocol = (AbstractAjpProtocol<?>) ajpConnector.getProtocolHandler();
        protocol.setSecretRequired(false);

        return ajpConnector;
    }

}

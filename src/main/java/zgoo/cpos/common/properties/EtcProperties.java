package zgoo.cpos.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fwaccess.properties")
@Data
public class EtcProperties {
    String tomcat_ajp_protocol;
    int tomcat_ajp_port;
    int tomcat_ajp_redirect_port;
    String tomcat_ajp_address;
    String tomcat_ajp_allowed_request_attributes_pattern;
}

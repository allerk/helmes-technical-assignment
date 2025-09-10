package ee.allerk.helmes_technical_assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "properties", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {
    private Boolean initData;
}

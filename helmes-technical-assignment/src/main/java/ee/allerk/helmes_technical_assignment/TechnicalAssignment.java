package ee.allerk.helmes_technical_assignment;

import ee.allerk.helmes_technical_assignment.config.ApplicationProperties;
import ee.allerk.helmes_technical_assignment.service.setup.Initializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class TechnicalAssignment {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TechnicalAssignment.class);
        Environment env = app.run(args).getEnvironment();
	}

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            ApplicationProperties applicationProperties = ctx.getBean(ApplicationProperties.class);
            if (applicationProperties.getInitData()) {
                Initializer initializer = ctx.getBean(Initializer.class);
                initializer.setupDatabase();
            }
        };
    }
}

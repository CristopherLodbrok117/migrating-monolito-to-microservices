package app.calendar_service.web.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sinaloa Backend",
                version = "2.0",
                description = "Documentación del back-end para el frontend"
        )
)
public class SwaggerConfig {
}

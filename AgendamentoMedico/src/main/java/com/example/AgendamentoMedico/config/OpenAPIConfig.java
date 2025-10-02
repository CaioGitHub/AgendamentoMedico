package com.example.AgendamentoMedico.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Agendamento Médico")
                        .version("v1")
                        .description("API para gerenciamento de consultas médicas, convênios, especialidades e agendamentos.")
                        .contact(new Contact()
                                .name("Equipe Agendamento Médico")
                                .email("suporte@agendamento.com")
                                .url("https://agendamento.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}

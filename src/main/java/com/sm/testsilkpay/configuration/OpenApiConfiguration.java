package com.sm.testsilkpay.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info = @Info(
        title = "SilkPay Test Project API",
        description = "Simple banking project for SilkPay",
        version = "1.0.0",
        contact = @Contact(name = "Makhmud Salakhedenov", email = "m.salakhedenov@gmail.com")
    )
)
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "JWT",
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfiguration {}

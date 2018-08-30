package com.egakat.io.solicitudes.gws.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.egakat.core.web.client.configuration.RestProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "com.cliente.integraciones.ordenes.alistamiento.rest")
@Getter
@Setter
@ToString
@Validated
public class OrdenAlistamientoClienteRestProperties implements RestProperties {

	private String basePath;
}

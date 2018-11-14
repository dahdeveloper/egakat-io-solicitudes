package com.egakat.io.gws.service.impl.solicitudes;

import java.util.List;

import org.springframework.stereotype.Service;

import com.egakat.io.commons.solicitudes.dto.SolicitudDespachoDto;
import com.egakat.io.core.dto.ActualizacionDto;
import com.egakat.io.core.dto.ErrorIntegracionDto;
import com.egakat.io.core.enums.EstadoIntegracionType;
import com.egakat.io.gws.client.constants.SolicitudEstadoConstants;

import lombok.val;

@Service
public class SolicitudesDespachoNotificacionAceptacionPushServiceImpl
		extends SolicitudesDespachoNotificacionPushServiceImpl<String> {

	@Override
	protected List<ActualizacionDto> getPendientes() {
		val result = getActualizacionesService().findAllByIntegracionAndEstadoIntegracionAndSubEstadoIntegracionIn(
				getIntegracion(), EstadoIntegracionType.CARGADO, SolicitudEstadoConstants.RECIBIDA_OPL);
		return result;
	}

	@Override
	protected String asOutput(SolicitudDespachoDto model, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		return "";
	}

	@Override
	protected Object push(String output, SolicitudDespachoDto model, ActualizacionDto actualizacion,
			List<ErrorIntegracionDto> errores) {
		val url = getUrl();
		val query = "/{id}?status={status}";

		getRestClient().put(url + query, output, Object.class, model.getIdExterno(),
				SolicitudEstadoConstants.ACEPTADA_OPL);
		return "";
	}

	@Override
	protected void onSuccess(Object response, String output, SolicitudDespachoDto model,
			ActualizacionDto actualizacion) {
		super.onSuccess(response, output, model, actualizacion);
		actualizacion.setSubEstadoIntegracion(SolicitudEstadoConstants.ACEPTADA_OPL);
	}
}
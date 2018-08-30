package com.egakat.io.solicitudes.gws.service.impl.crud;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egakat.core.services.crud.impl.CrudServiceImpl;
import com.egakat.io.solicitudes.gws.domain.ActualizacionIntegracion;
import com.egakat.io.solicitudes.gws.dto.ActualizacionIntegracionDto;
import com.egakat.io.solicitudes.gws.dto.ErrorIntegracionDto;
import com.egakat.io.solicitudes.gws.enums.EstadoIntegracionType;
import com.egakat.io.solicitudes.gws.enums.EstadoNotificacionType;
import com.egakat.io.solicitudes.gws.repository.ActualizacionIntegracionRepository;
import com.egakat.io.solicitudes.gws.service.api.crud.ActualizacionIntegracionCrudService;
import com.egakat.io.solicitudes.gws.service.api.crud.ErrorIntegracionCrudService;

import lombok.val;

@Service
public class ActualizacionIntegracionCrudServiceImpl
		extends CrudServiceImpl<ActualizacionIntegracion, ActualizacionIntegracionDto, Long>
		implements ActualizacionIntegracionCrudService {

	@Autowired
	private ErrorIntegracionCrudService erroresService;

	@Autowired
	private ActualizacionIntegracionRepository repository;

	@Override
	protected ActualizacionIntegracionRepository getRepository() {
		return repository;
	}

	@Override
	protected ActualizacionIntegracionDto asModel(ActualizacionIntegracion entity) {
		// @formatter:off
		val result = ActualizacionIntegracionDto
				.builder()
				.id(entity.getId())
				.integracion(entity.getIntegracion())
				.correlacion(entity.getCorrelacion())
				.idExterno(entity.getIdExterno())
				.estadoIntegracion(entity.getEstadoIntegracion())
				.estadoExterno(entity.getEstadoExterno())
				.estadoNotificacion(entity.getEstadoNotificacion())
				.entradasEnCola(entity.getEntradasEnCola())
				.arg0(entity.getArg0())
				.arg1(entity.getArg1())
				.arg2(entity.getArg2())
				.arg3(entity.getArg3())
				.arg4(entity.getArg4())
				.arg5(entity.getArg5())
				.arg6(entity.getArg6())
				.arg7(entity.getArg7())
				.arg8(entity.getArg8())
				.arg9(entity.getArg9())
				.datos(entity.getDatos())
				.version(entity.getVersion())
				.fechaCreacion(entity.getFechaCreacion())
				.fechaModificacion(entity.getFechaModificacion())
				.build();
		// @formatter:on
		return result;
	}

	@Override
	protected ActualizacionIntegracion asEntity(ActualizacionIntegracionDto model, ActualizacionIntegracion entity) {

		entity.setIntegracion(model.getIntegracion());
		entity.setCorrelacion(model.getCorrelacion());
		entity.setIdExterno(model.getIdExterno());
		entity.setEstadoIntegracion(model.getEstadoIntegracion());
		entity.setEstadoExterno(model.getEstadoExterno());
		entity.setEstadoNotificacion(model.getEstadoNotificacion());
		entity.setEntradasEnCola(model.getEntradasEnCola());
		entity.setArg0(model.getArg0());
		entity.setArg1(model.getArg1());
		entity.setArg2(model.getArg2());
		entity.setArg3(model.getArg3());
		entity.setArg4(model.getArg4());
		entity.setArg5(model.getArg5());
		entity.setArg6(model.getArg6());
		entity.setArg7(model.getArg7());
		entity.setArg8(model.getArg8());
		entity.setArg9(model.getArg9());
		entity.setDatos(model.getDatos());

		return entity;
	}

	@Override
	protected ActualizacionIntegracion newEntity() {
		return new ActualizacionIntegracion();
	}

	// ========================================================================================================
	@Override
	public ActualizacionIntegracionDto findOneByIntegracionAndCorrelacionAndIdExterno(String integracion,
			String correlacion, String idExterno) {
		val optional = getRepository().findByIntegracionAndCorrelacionAndIdExterno(integracion, correlacion, idExterno);

		if (!optional.isPresent()) {
			val format = "integracion=%s, correlacion=%s, id_externo=%s";
			throw new EntityNotFoundException(String.format(format, integracion, correlacion, idExterno));
		}
		val result = asModel(optional.get());
		return result;
	}

	@Override
	public List<ActualizacionIntegracionDto> findAllByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estadosIntegracion) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionIn(integracion, estadosIntegracion);
		val result = asModels(entities);
		return result;
	}

	@Override
	public List<ActualizacionIntegracionDto> findAllNoNotificadasByEstadoIntegracionIn(String integracion,
			EstadoIntegracionType... estadoIntegracion) {
		val entities = getRepository().findAllByIntegracionAndEstadoIntegracionInAndEstadoNotificacion(integracion,
				estadoIntegracion, EstadoNotificacionType.NOTIFICAR);
		val result = asModels(entities);
		return result;
	}

	@Override
	public void enqueue(ActualizacionIntegracionDto model) {
		val optional = getRepository().findByIntegracionAndCorrelacionAndIdExterno(model.getIntegracion(),
				model.getCorrelacion(), model.getIdExterno());

		if (!optional.isPresent()) {
			create(model);
		} else {
			val entity = optional.get();
			if (entity.getEntradasEnCola() == 0) {
				entity.setEntradasEnCola(entity.getEntradasEnCola() + 1);
				getRepository().saveAndFlush(entity);
			}
		}
	}

	@Override
	public void updateEstadoIntegracion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoIntegracionType ok, EstadoIntegracionType error) {

		if (errores.isEmpty()) {
			model.setEstadoIntegracion(ok);
		} else {
			model.setEstadoIntegracion(error);
		}
		model.setEstadoNotificacion(EstadoNotificacionType.NOTIFICAR);

		erroresService.create(errores);
		update(model);
	}

	@Override
	public void updateEstadoNotificacion(ActualizacionIntegracionDto model, List<ErrorIntegracionDto> errores,
			EstadoNotificacionType ok, EstadoNotificacionType error) {
		if (errores.isEmpty()) {
			model.setEstadoNotificacion(ok);
			// TODO ¿en este punto se debe programar una notificacion?
		} else {
			model.setEstadoNotificacion(error);
			// TODO se debe hacer un manejo de re intento de notificacion en caso de error,
			// esto se podria manejar mejor en las notificaciones
		}

		erroresService.create(errores);
		update(model);
	}
}
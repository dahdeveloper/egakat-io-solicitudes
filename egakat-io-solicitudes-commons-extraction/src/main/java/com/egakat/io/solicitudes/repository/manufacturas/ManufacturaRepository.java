package com.egakat.io.solicitudes.repository.manufacturas;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.egakat.integration.files.enums.EstadoRegistroType;
import com.egakat.integration.files.repository.RegistroRepository;
import com.egakat.io.solicitudes.domain.manufacturas.Manufactura;

public interface ManufacturaRepository extends RegistroRepository<Manufactura> {

	@Override
	@Query("SELECT DISTINCT a.idArchivo FROM Manufactura a WHERE a.estado IN (:estados) ORDER BY a.idArchivo")
	List<Long> findAllArchivoIdByEstadoIn(@Param("estados") List<EstadoRegistroType> estado);
}

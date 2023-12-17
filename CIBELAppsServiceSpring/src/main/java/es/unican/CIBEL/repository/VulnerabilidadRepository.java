package es.unican.CIBEL.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.CIBEL.domain.Vulnerabilidad;

public interface VulnerabilidadRepository extends JpaRepository<Vulnerabilidad, String> {
	
}

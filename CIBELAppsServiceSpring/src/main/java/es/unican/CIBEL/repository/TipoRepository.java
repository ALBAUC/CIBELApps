package es.unican.CIBEL.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.CIBEL.domain.Tipo;

public interface TipoRepository extends JpaRepository<Tipo, Long> {
	Tipo findByNombre(String nombre);
}

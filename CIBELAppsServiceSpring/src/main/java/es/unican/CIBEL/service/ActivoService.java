package es.unican.CIBEL.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Activo;
import es.unican.CIBEL.repository.ActivoRepository;

@Service
public class ActivoService {
	
	@Autowired
	private ActivoRepository repository;
	
	public List<Activo> activos() {
		return repository.findAll();
	}
	
	public List<Activo> activosPorTipo(Tipo tipo) {
		return repository.findByTipo(tipo);
	}
	
	public Activo buscaActivo(String nombre) {
		return repository.findByNombre(nombre);
	}
}

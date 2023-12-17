package es.unican.CIBEL.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.repository.TipoRepository;

@Service
public class TipoService {
	
	@Autowired
	private TipoRepository repository;
	
	public List<Tipo> tipos() {
		return repository.findAll();
	}
	
	public Tipo buscaTipo(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public Tipo buscaTipoPorNombre(String nombre) {
		return repository.findByNombre(nombre);
	}

}

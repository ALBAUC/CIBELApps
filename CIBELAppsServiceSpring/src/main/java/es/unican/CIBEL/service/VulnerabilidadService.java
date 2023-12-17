package es.unican.CIBEL.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.repository.VulnerabilidadRepository;

@Service
public class VulnerabilidadService {
	
	@Autowired
	private VulnerabilidadRepository repository;
	
	public List<Vulnerabilidad> vulnerabilidades() {
		return repository.findAll();
	}

}

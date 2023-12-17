package es.unican.CIBEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.service.VulnerabilidadService;

@RestController
@RequestMapping("vulnerabilidades")
public class VulnerabilidadController {
	
	@Autowired
	private VulnerabilidadService vulnerabilidadService;
	
	@GetMapping
	public List<Vulnerabilidad> getVulnerabilidades() {
		return vulnerabilidadService.vulnerabilidades();
	}

}

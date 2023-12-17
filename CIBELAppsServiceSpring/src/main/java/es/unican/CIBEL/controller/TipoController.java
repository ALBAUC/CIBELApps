package es.unican.CIBEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.service.TipoService;

@RestController
@RequestMapping("/tipos")
public class TipoController {
	
	@Autowired
	private TipoService tipoService;
	
	@GetMapping
	public List<Tipo> getTipos() {
		return tipoService.tipos();
	}

}

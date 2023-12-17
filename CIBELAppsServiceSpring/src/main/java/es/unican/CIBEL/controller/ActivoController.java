package es.unican.CIBEL.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Activo;
import es.unican.CIBEL.service.TipoService;
import es.unican.CIBEL.service.ActivoService;

@RestController
@RequestMapping("activos")
public class ActivoController {
	
	@Autowired
	private ActivoService activosService;
	
	@Autowired
	private TipoService tipoService;
	
	@GetMapping
	public List<Activo> getActivos(@RequestParam(value = "tipo", required = false) String tipo) {
		
		List<Activo> activos = new LinkedList<Activo>();
		
		if (tipo != null) {
			activos = activosService.activosPorTipo(tipoService.buscaTipoPorNombre(tipo));
		} else {
			activos = activosService.activos();
		}
		
		return activos;
	}
	
	@GetMapping("/{nombre}")
	public ResponseEntity<Activo> getActivo(@PathVariable String nombre) {
		ResponseEntity<Activo> result;
		Activo activo = activosService.buscaActivo(nombre);
		
		if (activo == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(activo);
		}
		return result;
	}
}

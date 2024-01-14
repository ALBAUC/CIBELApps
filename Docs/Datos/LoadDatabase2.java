package es.unican.CIBEL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.unican.CIBEL.domain.Activo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.repository.ActivoRepository;
import es.unican.CIBEL.repository.TipoRepository;
import es.unican.CIBEL.repository.VulnerabilidadRepository;

@Configuration
public class LoadDatabase {

	  @Bean
	  CommandLineRunner initDatabase(
			  TipoRepository tipoRepo,
			  ActivoRepository activoRepo,
			  VulnerabilidadRepository cvesRepo) {

	    return args -> {
            
            // Cargar tipos y aplicaciones del archivo
	        try (BufferedReader br = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELApps/Docs/Datos/Aplicaciones.txt"))) {
	            String line;
	            Tipo tipoActual = null;

	            while ((line = br.readLine()) != null) {
	                if (line.startsWith("#")) {
	                    // Nuevo tipo de aplicacion
	                    String tipoNombre = line.substring(1).trim();
	                    tipoActual = new Tipo(tipoNombre);
	                    tipoRepo.save(tipoActual);
	                } else if (!line.isEmpty() && tipoActual != null) {
	                    // Nueva aplicacion
	                    String[] aplicacionInfo = line.split(", ");
	                    String nombreApp = aplicacionInfo[0];
	                    String icono = aplicacionInfo[1];
	                    Activo activo = new Activo(nombreApp, icono, tipoActual);
	                    activoRepo.save(activo);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    	
	    	
	    	// Cargar vulnerabiliades del json
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	JsonNode rootNode = objectMapper.readTree(new File("/Users/alinasolonarubotnari/ALBAUC/CIBELApps/Docs/Datos/apps_cves2.json"));

	    	for (JsonNode vulnDataNode : rootNode) {
	    	    String nombre = vulnDataNode.get("APP").asText();

	    	    Vulnerabilidad v = new Vulnerabilidad();
	    	    v.setIdCVE(vulnDataNode.get("CVE ID").asText());
	    	    v.setDescripcion(vulnDataNode.get("Second Description").asText());
	    	    v.setConfidentialityImpact(vulnDataNode.get("Confidentiality Impact").asText());
	    	    v.setIntegrityImpact(vulnDataNode.get("Integrity Impact").asText());
	    	    v.setAvailabilityImpact(vulnDataNode.get("Availability Impact").asText());
	    	    if (vulnDataNode.has("Base Score") && !vulnDataNode.get("Base Score").isNull()) {
	    	        v.setBaseScore(vulnDataNode.get("Base Score").asDouble());
	    	    } else {
	    	        v.setBaseScore(0.0);
	    	    }
	    	    v.setBaseSeverity(vulnDataNode.get("Base Severity").asText());
	    	    
	    	    String versionEndExcluding = vulnDataNode.get("Version End Excluding").asText();
	    	    if (versionEndExcluding.trim().isEmpty()) {
	    	    	v.setVersionEndExcluding(null);
	    	    } else {
	    	    	v.setVersionEndExcluding(versionEndExcluding);
	    	    }
	    	    
	    	    String versionEndIncluding = vulnDataNode.get("Version End Including").asText();
	    	    if (versionEndIncluding.trim().isEmpty()) {
	    	    	v.setVersionEndIncluding(null);
	    	    } else {
	    	    	v.setVersionEndIncluding(versionEndIncluding);
	    	    }

	    	    cvesRepo.save(v);
	    	    
	    	    //System.out.println(nombre + ": "  + v.getIdCVE());

	    	    Activo a = activoRepo.findByNombre(nombre);
	    	    a.getVulnerabilidades().add(v);
	    	    activoRepo.save(a);
	    	}
	    	
	    };
	  }

}

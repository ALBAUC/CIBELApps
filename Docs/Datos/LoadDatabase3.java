package es.unican.CIBEL;

import java.io.FileReader;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.CSVReader;

import es.unican.CIBEL.domain.Activo;
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
	    	
	    	// Añadir descripcion en ingles de los cves
	    	CSVReader reader = new CSVReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELApps/Docs/Datos/Vulnerabilidades.csv"));
	    	List<String[]> rows = reader.readAll();
	    	
	    	for (String[] row : rows) {
	    		if (row[0].equals("Modelo")) {
	    	        continue; // Saltar la fila de encabezado
	    	    }
	    		
	    		Vulnerabilidad v = cvesRepo.findById(row[1]).orElse(null);
	    		if (v != null) {
	    			v.setDescripcion_en(row[3]);
		    		cvesRepo.save(v);
	    		} else {
	    			Activo a = activoRepo.findByNombre(row[0]);
	    			if (a != null) {
	    				Vulnerabilidad vN = new Vulnerabilidad();
			    		vN.setIdCVE(row[1]);
			    		vN.setDescripcion(row[2]);
			    		vN.setDescripcion_en(row[3]);
			    		vN.setConfidentialityImpact(row[6]);
			    		vN.setIntegrityImpact(row[7]);
			    		vN.setAvailabilityImpact(row[8]);
			    		if (row[9] != null && !row[9].isEmpty()) {
			    		    vN.setBaseScore(Double.parseDouble(row[9]));
			    		} else {
			    		    vN.setBaseScore(0);
			    		}

			    		vN.setBaseSeverity(row[10]);
			    		
			    		cvesRepo.save(vN);
			    		
			    		
			    		a.getVulnerabilidades().add(vN);
			    		activoRepo.save(a);
	    			}
	    		}
	    	}
	    	
	    };
	  }
}

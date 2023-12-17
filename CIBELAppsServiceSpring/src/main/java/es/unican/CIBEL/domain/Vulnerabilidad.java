package es.unican.CIBEL.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vulnerabilidad {
	
	@Id
	private String idCVE;
	
	@Column(length = 5000)
	private String descripcion;
	
    private String confidentialityImpact;
    
    private String integrityImpact;
    
    private String availabilityImpact;
    
    private double baseScore;
    
    private String baseSeverity;
    
    public Vulnerabilidad() {}

	public Vulnerabilidad(String idCVE, String descripcion, String confidentialityImpact, String integrityImpact,
			String availabilityImpact, double baseScore, String baseSeverity) {
		this.idCVE = idCVE;
		this.descripcion = descripcion;
		this.confidentialityImpact = confidentialityImpact;
		this.integrityImpact = integrityImpact;
		this.availabilityImpact = availabilityImpact;
		this.baseScore = baseScore;
		this.baseSeverity = baseSeverity;
	}

	public String getIdCVE() {
		return idCVE;
	}

	public void setIdCVE(String idCVE) {
		this.idCVE = idCVE;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getConfidentialityImpact() {
		return confidentialityImpact;
	}

	public void setConfidentialityImpact(String confidentialityImpact) {
		this.confidentialityImpact = confidentialityImpact;
	}

	public String getIntegrityImpact() {
		return integrityImpact;
	}

	public void setIntegrityImpact(String integrityImpact) {
		this.integrityImpact = integrityImpact;
	}

	public String getAvailabilityImpact() {
		return availabilityImpact;
	}

	public void setAvailabilityImpact(String availabilityImpact) {
		this.availabilityImpact = availabilityImpact;
	}

	public double getBaseScore() {
		return baseScore;
	}

	public void setBaseScore(double baseScore) {
		this.baseScore = baseScore;
	}

	public String getBaseSeverity() {
		return baseSeverity;
	}

	public void setBaseSeverity(String baseSeverity) {
		this.baseSeverity = baseSeverity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(availabilityImpact, baseScore, baseSeverity, confidentialityImpact, idCVE, integrityImpact);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vulnerabilidad other = (Vulnerabilidad) obj;
		return Objects.equals(availabilityImpact, other.availabilityImpact)
				&& Double.doubleToLongBits(baseScore) == Double.doubleToLongBits(other.baseScore)
				&& Objects.equals(baseSeverity, other.baseSeverity)
				&& Objects.equals(confidentialityImpact, other.confidentialityImpact)
				&& Objects.equals(idCVE, other.idCVE) && Objects.equals(integrityImpact, other.integrityImpact);
	}

}

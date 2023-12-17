package es.unican.cibel.activities.activos.detail.cve;

import es.unican.cibel.model.Vulnerabilidad;

public class CveDetailPresenter implements ICveDetailContract.Presenter {

    private final ICveDetailContract.View view;
    private Vulnerabilidad vulnerabilidad;

    public CveDetailPresenter(Vulnerabilidad vulnerabilidad, CveDetailView cveDetailView) {
        this.view = cveDetailView;
        this.vulnerabilidad = vulnerabilidad;
    }

    @Override
    public void init() {

    }

    @Override
    public double getCveBaseScore() {
        return vulnerabilidad.getBaseScore();
    }

    @Override
    public String getCveId() {
        return vulnerabilidad.getIdCVE();
    }

    @Override
    public String getCveDescripcion() {
        return vulnerabilidad.getDescripcion();
    }

    @Override
    public String getCveConfidenciality() {
        return vulnerabilidad.getConfidentialityImpact();
    }

    @Override
    public String getCveIntegrity() {
        return vulnerabilidad.getIntegrityImpact();
    }

    @Override
    public String getCveAvailability() {
        return vulnerabilidad.getAvailabilityImpact();
    }

    @Override
    public int mapImpact(String impact) {
        return vulnerabilidad.mapImpact(impact);
    }
}

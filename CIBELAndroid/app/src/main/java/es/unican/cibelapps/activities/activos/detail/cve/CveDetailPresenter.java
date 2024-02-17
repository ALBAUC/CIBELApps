package es.unican.cibelapps.activities.activos.detail.cve;

import java.util.Locale;

import es.unican.cibelapps.model.Vulnerabilidad;

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
        String result = "";
        Locale locale = view.getMyApplication().getResources().getConfiguration().getLocales().get(0);
        String language = locale.getLanguage();
        if (language.equals("es")) {
            result = vulnerabilidad.getDescripcion();
        } else if (language.equals("en")) {
            result = vulnerabilidad.getDescripcion_en();
        }
        return result;

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

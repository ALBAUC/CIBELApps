package es.unican.cibel.activities.activos.detail.cve;

import es.unican.cibel.common.MyApplication;

public interface ICveDetailContract {

    interface View {
        MyApplication getMyApplication();
    }

    interface Presenter {
        void init();

        double getCveBaseScore();

        String getCveId();

        String getCveDescripcion();

        String getCveConfidenciality();

        String getCveIntegrity();

        String getCveAvailability();

        int mapImpact(String impact);
    }
}

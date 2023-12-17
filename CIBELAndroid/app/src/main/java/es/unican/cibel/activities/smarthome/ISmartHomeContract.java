package es.unican.cibel.activities.smarthome;

import java.util.ArrayList;
import java.util.List;

import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Vulnerabilidad;

public interface ISmartHomeContract {
    interface Presenter {
        void init();

        List<Activo> getActivosPerfil();

        List<Activo> getActivosPerfilOrdenadosPorSeguridadAsc();

        ArrayList getEntries();

        List<Vulnerabilidad> getVulnerabilidadesPerfil();

        int getSecurityRatingHome();

        List<Activo> getActivosPerfilOrdenadosPorSeguridadDesc();
    }

    interface  View {
        MyApplication getMyApplication();
    }
}

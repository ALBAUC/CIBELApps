package es.unican.cibelapps.activities.smarthome;

import java.util.ArrayList;
import java.util.List;

import es.unican.cibelapps.common.MyApplication;
import es.unican.cibelapps.model.Activo;
import es.unican.cibelapps.model.Vulnerabilidad;

public interface ISmartHomeContract {
    interface Presenter {
        void init();

        List<Activo> getActivosPerfil();

        List<Activo> getActivosPerfilOrdenadosPorSeguridadAsc();

        int getSecurityRatingHome();

        List<Activo> getActivosPerfilOrdenadosPorSeguridadDesc();
    }

    interface  View {
        MyApplication getMyApplication();
    }
}

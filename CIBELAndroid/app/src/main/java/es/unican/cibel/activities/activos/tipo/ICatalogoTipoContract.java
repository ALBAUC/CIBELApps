package es.unican.cibel.activities.activos.tipo;

import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import java.util.List;

public interface ICatalogoTipoContract {
    interface View {
        MyApplication getMyApplication();
    }

    interface Presenter {

        void init();

        String getTipoName();

        List<Activo> getActivosDeTipo();

        List<Activo> getActivosTipoOrdenadosPorSeguridadDesc();

        List<Activo> getActivosTipoOrdenadosPorSeguridadAsc();

        List<Activo> getActivosPerfil();

        Activo getAssetByName(String appName);
    }
}

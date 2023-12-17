package es.unican.cibelapps.activities.activos.tipo;

import es.unican.cibelapps.common.MyApplication;
import es.unican.cibelapps.model.Activo;
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

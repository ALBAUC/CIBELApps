package es.unican.cibelapps.activities.activos.search;

import java.util.List;

import es.unican.cibelapps.common.MyApplication;
import es.unican.cibelapps.model.Activo;

public interface ISearchResultContract {

    interface View {
        MyApplication getMyApplication();
    }

    interface Presenter {

        void init();

        List<Activo> doSearch(String query);

        Activo getAssetByName(String appName);

        List<Activo> getPerfilAssets();

        List<Activo> getActivosOrdenadosPorSeguridadDesc(String query);

        List<Activo> getActivosOrdenadosPorSeguridadAsc(String query);
    }
}

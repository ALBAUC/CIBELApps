package es.unican.cibel.activities.activos;

import java.util.List;

import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Tipo;

/**
 * Interfaz que define los métodos que deben ser implementados por el presentador y la vista
 * de la pestaña Apps.
 */
public interface ICatalogoContract {
    interface View {
        /**
         * Devuelve una instancia de MyApplication.
         * @return MyApplication.
         */
        MyApplication getMyApplication();

        /**
         * Se solicita a la vista que muestre una alerta informando que hubo un
         * error cargando las aplicaciones.
         */
        void showLoadError();

        /**
         * Se solicita a la vista que muestre una alerta informando que las
         * aplicaciones han sido cargadas correctaemnte.
         * @param appsCount el numero de aplicaciones que fueron cargadas
         */
        void showLoadCorrect(int appsCount);
    }

    interface Presenter {
        /**
         * Inicializa las DAO y los datos de la base de datos.
         */
        void init();

        List<Activo> getPerfilAssets();

        Activo getAssetByName(String appName);

        List<Tipo> getTipos();

        List<Activo> getAllActivos();
    }
}

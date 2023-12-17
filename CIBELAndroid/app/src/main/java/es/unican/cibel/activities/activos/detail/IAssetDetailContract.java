package es.unican.cibel.activities.activos.detail;

import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Vulnerabilidad;

/**
 * Interfaz que define los métodos que deben ser implementados por el presentador y la vista
 * del detalle de un activo.
 */
public interface IAssetDetailContract {
    interface  View {

        MyApplication getMyApplication();
    }

    interface Presenter {

        void init();

        String getAssetIcon();

        String getAssetName();

        String getAssetType();

        void onAddAssetClicked();

        boolean isAssetAdded();

        List<Activo> getPerfilAssets();

        List<Vulnerabilidad> getAssetCves();

        int getSecurityRating();

        List<Vulnerabilidad> getAssetCvesOrdenadorPorFechaRec();

        List<PieEntry> getEntries();

        List<Vulnerabilidad> getAssetCvesOrdenadorPorFechaAnt();

        List<Vulnerabilidad> getAssetCvesOrdenadorPorGravedadAsc();

        List<Vulnerabilidad> getAssetCvesOrdenadorPorGravedadDesc();
    }
}

package es.unican.cibel.activities.smarthome;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Perfil;
import es.unican.cibel.model.Vulnerabilidad;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.PerfilDao;
import es.unican.cibel.repository.db.VulnerabilidadDao;

public class SmartHomePresenter implements ISmartHomeContract.Presenter {
    private final ISmartHomeContract.View view;
    private Perfil perfil;
    private ActivoDao activoDao;
    private VulnerabilidadDao vulnerabilidadDao;

    public SmartHomePresenter(ISmartHomeContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        DaoSession daoSession = view.getMyApplication().getDaoSession();
        PerfilDao perfilDao = daoSession.getPerfilDao();
        perfil = Perfil.getInstance(perfilDao);
        activoDao = daoSession.getActivoDao();
        vulnerabilidadDao = daoSession.getVulnerabilidadDao();
    }

    @Override
    public List<Activo> getActivosPerfil() {
        List<Activo> perfilAssets = activoDao._queryPerfil_ActivosAnhadidos(perfil.getId());
        return perfilAssets;
    }

    @Override
    public ArrayList getEntries() {
        int numCritical = 0;
        int numHigh = 0;
        int numMedium = 0;
        int numLow = 0;
        for (Activo a : getActivosPerfil()) {
            List<Vulnerabilidad> assetVulnerabilities = getVulnerabilidadesPorActivo(a);
            for (Vulnerabilidad v : assetVulnerabilities) {
                String baseSeverity = v.getBaseSeverity();
                if (baseSeverity != null) {
                    if (baseSeverity.equals(Vulnerabilidad.SEVERITY_C)) {
                        numCritical++;
                    } else if (baseSeverity.equals(Vulnerabilidad.SEVERITY_H)) {
                        numHigh++;
                    } else if (baseSeverity.equals(Vulnerabilidad.SEVERITY_M)) {
                        numMedium++;
                    } else if (baseSeverity.equals(Vulnerabilidad.SEVERITY_L)) {
                        numLow++;
                    }
                }
            }
        }
        ArrayList pieEntries = new ArrayList();
        pieEntries.add(new PieEntry(numCritical, Vulnerabilidad.ES_SEVERITY_C));
        pieEntries.add(new PieEntry(numHigh, Vulnerabilidad.ES_SEVERITY_H));
        pieEntries.add(new PieEntry(numMedium, Vulnerabilidad.ES_SEVERITY_M));
        pieEntries.add(new PieEntry(numLow, Vulnerabilidad.ES_SEVERITY_L));
        return pieEntries;
    }

    private List<Vulnerabilidad> getVulnerabilidadesPorActivo(Activo activo) {
        return vulnerabilidadDao._queryActivo_Vulnerabilidades(activo.getIdActivo());
    }

    @Override
    public List<Vulnerabilidad> getVulnerabilidadesPerfil() {
        List<Vulnerabilidad> vulnerabilidadesPerfil = new LinkedList<>();
        for (Activo a : getActivosPerfil()) {
            vulnerabilidadesPerfil.addAll(getVulnerabilidadesPorActivo(a));
        }
        return vulnerabilidadesPerfil;
    }

    @Override
    public int getSecurityRatingHome() {
        List<Activo> activos = getActivosPerfil();
        int result = 100;
        if (activos.size() != 0) {
            double s = 0;
            for (Activo a : activos) {
                s += a.calcularPuntuacionSeguridad();
            }
            result = (int) Math.round(s / activos.size());
        }
        return result;
    }

    @Override
    public List<Activo> getActivosPerfilOrdenadosPorSeguridadAsc() {
        List<Activo> perfilAssets = getActivosPerfil();
        Collections.sort(perfilAssets, new Comparator<Activo>() {
            @Override
            public int compare(Activo activo1, Activo activo2) {
                int gravedad1 = (int) Math.round(activo1.calcularTotalGravedad());
                int gravedad2 = (int) Math.round(activo2.calcularTotalGravedad());
                return Integer.compare(gravedad2, gravedad1);
            }
        });

        return perfilAssets;
    }

    @Override
    public List<Activo> getActivosPerfilOrdenadosPorSeguridadDesc() {
        List<Activo> perfilAssets = getActivosPerfil();
        Collections.sort(perfilAssets, new Comparator<Activo>() {
            @Override
            public int compare(Activo activo1, Activo activo2) {
                int gravedad1 = (int) Math.round(activo1.calcularTotalGravedad());
                int gravedad2 = (int) Math.round(activo2.calcularTotalGravedad());
                return Integer.compare(gravedad1, gravedad2);
            }
        });

        return perfilAssets;
    }
}

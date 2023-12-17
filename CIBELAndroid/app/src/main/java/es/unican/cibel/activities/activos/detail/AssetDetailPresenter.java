package es.unican.cibel.activities.activos.detail;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Perfil;
import es.unican.cibel.model.Vulnerabilidad;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.PerfilDao;
import es.unican.cibel.repository.db.VulnerabilidadDao;

public class AssetDetailPresenter implements IAssetDetailContract.Presenter {

    private final IAssetDetailContract.View view;
    private ActivoDao activoDao;
    private PerfilDao perfilDao;
    private VulnerabilidadDao vulnerabilidadDao;
    private final Activo activo;
    private Perfil perfil;

    public AssetDetailPresenter(Activo activo, IAssetDetailContract.View view) {
        this.activo = activo;
        this.view = view;
    }

    @Override
    public void init() {
        DaoSession daoSession = view.getMyApplication().getDaoSession();
        activoDao = daoSession.getActivoDao();
        perfilDao = daoSession.getPerfilDao();
        vulnerabilidadDao = daoSession.getVulnerabilidadDao();
        perfil = Perfil.getInstance(perfilDao);
    }

    @Override
    public String getAssetIcon() {
        return activo.getIcono();
    }

    @Override
    public String getAssetName() {
        return activo.getNombre();
    }

    @Override
    public String getAssetType() {
        return activo.getTipo().getNombre();
    }

    @Override
    public void onAddAssetClicked() {
        if (!isAssetAdded()) {
            activo.setFk_perfil(perfil.getId());
            perfil.getActivosAnhadidos().add(activo);
            activoDao.update(activo);
            perfilDao.update(perfil);
        } else {
            activo.setFk_perfil(null);
            perfil.getActivosAnhadidos().remove(activo);
            activoDao.update(activo);
            perfilDao.update(perfil);
        }
    }

    @Override
    public boolean isAssetAdded() {
        return getPerfilAssets().contains(activo);
    }

    @Override
    public List<Activo> getPerfilAssets() {
        List<Activo> perfilAssets = activoDao._queryPerfil_ActivosAnhadidos(perfil.getId());
        return perfilAssets;
    }

    @Override
    public List<Vulnerabilidad> getAssetCves() {
        List<Vulnerabilidad> assetCves = vulnerabilidadDao._queryActivo_Vulnerabilidades(activo.getIdActivo());
        return assetCves;
    }

    @Override
    public int getSecurityRating() {
        return activo.calcularPuntuacionSeguridad();
    }


    @Override
    public List<PieEntry> getEntries() {
        int numCritical = 0;
        int numHigh = 0;
        int numMedium = 0;
        int numLow = 0;
        for (Vulnerabilidad v : getAssetCves()) {
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
        ArrayList pieEntries = new ArrayList();
        pieEntries.add(new PieEntry(numCritical, Vulnerabilidad.ES_SEVERITY_C));
        pieEntries.add(new PieEntry(numHigh, Vulnerabilidad.ES_SEVERITY_H));
        pieEntries.add(new PieEntry(numMedium, Vulnerabilidad.ES_SEVERITY_M));
        pieEntries.add(new PieEntry(numLow, Vulnerabilidad.ES_SEVERITY_L));
        return pieEntries;
    }

    @Override
    public List<Vulnerabilidad> getAssetCvesOrdenadorPorFechaRec() {
        List<Vulnerabilidad> assetCves = getAssetCves();
        Collections.sort(assetCves, new Comparator<Vulnerabilidad>() {
            @Override
            public int compare(Vulnerabilidad cve1, Vulnerabilidad cve2) {
                // Extraer los años y los identificadores
                String[] parts1 = cve1.getIdCVE().split("-");
                String[] parts2 = cve2.getIdCVE().split("-");

                int year1 = Integer.parseInt(parts1[1]);
                int year2 = Integer.parseInt(parts2[1]);

                int id1 = Integer.parseInt(parts1[2]);
                int id2 = Integer.parseInt(parts2[2]);

                // Comparar primero por año, y luego por identificador dentro del mismo año
                if (year1 != year2) {
                    return Integer.compare(year2, year1);
                } else {
                    return Integer.compare(id2, id1);
                }
            }
        });

        return assetCves;
    }

    @Override
    public List<Vulnerabilidad> getAssetCvesOrdenadorPorFechaAnt() {
        List<Vulnerabilidad> assetCves = getAssetCves();
        Collections.sort(assetCves, new Comparator<Vulnerabilidad>() {
            @Override
            public int compare(Vulnerabilidad cve1, Vulnerabilidad cve2) {
                // Extraer los años y los identificadores
                String[] parts1 = cve1.getIdCVE().split("-");
                String[] parts2 = cve2.getIdCVE().split("-");

                int year1 = Integer.parseInt(parts1[1]);
                int year2 = Integer.parseInt(parts2[1]);

                int id1 = Integer.parseInt(parts1[2]);
                int id2 = Integer.parseInt(parts2[2]);

                // Comparar primero por año, y luego por identificador dentro del mismo año
                if (year1 != year2) {
                    return Integer.compare(year1, year2);
                } else {
                    return Integer.compare(id1, id2);
                }
            }
        });

        return assetCves;
    }

    @Override
    public List<Vulnerabilidad> getAssetCvesOrdenadorPorGravedadAsc() {
        List<Vulnerabilidad> assetCves = getAssetCves();
        Collections.sort(assetCves, new Comparator<Vulnerabilidad>() {
            @Override
            public int compare(Vulnerabilidad v1, Vulnerabilidad v2) {
                int gravedad1 = (int) v1.getBaseScore() * 10;
                int gravedad2 = (int) v2.getBaseScore() * 10;
                return Integer.compare(gravedad1, gravedad2);
            }
        });
        return assetCves;
    }

    @Override
    public List<Vulnerabilidad> getAssetCvesOrdenadorPorGravedadDesc() {
        List<Vulnerabilidad> assetCves = getAssetCves();
        Collections.sort(assetCves, new Comparator<Vulnerabilidad>() {
            @Override
            public int compare(Vulnerabilidad v1, Vulnerabilidad v2) {
                int gravedad1 = (int) v1.getBaseScore() * 10;
                int gravedad2 = (int) v2.getBaseScore() * 10;
                return Integer.compare(gravedad2, gravedad1);
            }
        });
        return assetCves;
    }
}

package es.unican.cibelapps.activities.activos;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import es.unican.cibelapps.common.Callback;
import es.unican.cibelapps.model.Activo;
import es.unican.cibelapps.model.Perfil;
import es.unican.cibelapps.model.Tipo;
import es.unican.cibelapps.model.Vulnerabilidad;
import es.unican.cibelapps.repository.cibel.CibelRepository;
import es.unican.cibelapps.repository.cibel.ICibelRepository;
import es.unican.cibelapps.repository.db.ActivoDao;
import es.unican.cibelapps.repository.db.DaoSession;
import es.unican.cibelapps.repository.db.PerfilDao;
import es.unican.cibelapps.repository.db.TipoDao;

public class CatalogoPresenter implements ICatalogoContract.Presenter {

    private final ICatalogoContract.View view;
    private ActivoDao activoDao;
    private TipoDao tipoDao;
    private PerfilDao perfilDao;
    private ICibelRepository cibelRepository;
    private Perfil perfil;

    public CatalogoPresenter(ICatalogoContract.View view) {
        this.view = view;
    }

    // Para tests
    public CatalogoPresenter(ICatalogoContract.View view, ICibelRepository cibelRepository) {
        this.view = view;
        this.cibelRepository = cibelRepository;
    }

    @Override
    public void init() {
        DaoSession daoSession = view.getMyApplication().getDaoSession();
        activoDao = daoSession.getActivoDao();
        tipoDao = daoSession.getTipoDao();
        perfilDao = daoSession.getPerfilDao();
        perfil = Perfil.getInstance(perfilDao);

        cibelRepository = new CibelRepository(view.getMyApplication());
        if (cibelRepository.lastDownloadOlderThan(30, CibelRepository.KEY_LAST_SAVED_A)) {
            doSyncInit();
        }
    }

    private void doAsyncInit() {
        cibelRepository.requestTipos(new Callback<Tipo[]>() {
            @Override
            public void onSuccess(Tipo[] data) {

                cibelRepository.requestVulnerabilidades(new Callback<Vulnerabilidad[]>() {
                    @Override
                    public void onSuccess(Vulnerabilidad[] data) {
                        cibelRepository.requestActivos(new Callback<Activo[]>() {
                            @Override
                            public void onSuccess(Activo[] activos) {
                                view.showLoadCorrect(activos.length);
                            }

                            @Override
                            public void onFailure() {
                                view.showLoadError();
                            }
                        }, null);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });

    }

    private void doSyncInit() {
        if (cibelRepository.getTipos() == null ||
                cibelRepository.getVulnerabilidades() == null ||
                cibelRepository.getActivos(null) == null) {
            view.showLoadError();
        } else {
            view.showLoadCorrect((int) activoDao.count());
        }
    }

    @Override
    public List<Activo> getAllActivos() {
        return activoDao.loadAll();
    }

    @Override
    public List<Activo> cargarAppsAutomatico() {
        List<Activo> appsCargadas = new ArrayList<>();
        PackageManager pm = view.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packageInfo : apps) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String nombreAppEnDispositivo = packageInfo.applicationInfo.loadLabel(pm).toString();
                Activo mejorCoincidencia = encontrarCoincidenciaPorNombre(nombreAppEnDispositivo);
                if (mejorCoincidencia != null) {
                    mejorCoincidencia.setFk_perfil(perfil.getId());
                    perfil.getActivosAnhadidos().add(mejorCoincidencia);
                    activoDao.update(mejorCoincidencia);
                    perfilDao.update(perfil);
                    appsCargadas.add(mejorCoincidencia);
                }
            }
        }
        return appsCargadas;
    }

    private Activo encontrarCoincidenciaPorNombre(String nombreDispositivo) {
        List<Activo> appsEnCatalogo = getAllActivos();

        for (Activo appEnCatalogo : appsEnCatalogo) {
            String nombreAppEnCatalogo = appEnCatalogo.getNombre().toLowerCase();
            String nombreAppEnDispositivo = nombreDispositivo.toLowerCase();

            // Coincidencia exacta
            if (nombreAppEnCatalogo.equals(nombreAppEnDispositivo)) {
                return appEnCatalogo;
            }
        }

        // Buscar coincidencias parciales si no se encuentra una coincidencia exacta
        for (Activo appEnCatalogo : appsEnCatalogo) {
            String nombreAppEnCatalogo = appEnCatalogo.getNombre().toLowerCase();
            String nombreAppEnDispositivo = nombreDispositivo.toLowerCase();

            if (nombreAppEnCatalogo.contains(nombreAppEnDispositivo) || nombreAppEnDispositivo.contains(nombreAppEnCatalogo)) {
                return appEnCatalogo;
            }
        }

        return null;
    }


    public static double calcularSimilitudLevenshtein(String str1, String str2) {
        int len1 = str1.length() + 1;
        int len2 = str2.length() + 1;

        int[][] distancia = new int[len1][len2];

        for (int i = 0; i < len1; i++) {
            distancia[i][0] = i;
        }

        for (int j = 0; j < len2; j++) {
            distancia[0][j] = j;
        }

        for (int i = 1; i < len1; i++) {
            for (int j = 1; j < len2; j++) {
                int costo = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                distancia[i][j] = Math.min(Math.min(distancia[i - 1][j] + 1, distancia[i][j - 1] + 1), distancia[i - 1][j - 1] + costo);
            }
        }

        int maxLen = Math.max(len1, len2);
        double similitud = 1.0 - (double) distancia[len1 - 1][len2 - 1] / maxLen;
        return similitud;
    }

    @Override
    public List<Activo> getPerfilAssets() {
        List<Activo> result = null;
        try {
            result = activoDao._queryPerfil_ActivosAnhadidos(perfil.getId());
        } catch (SQLiteException e) {
        }
        return result;
    }

    @Override
    public Activo getAssetByName(String appName) {
        return activoDao.queryBuilder().where(ActivoDao.Properties.Nombre.like(appName)).unique();
    }

    @Override
    public List<Tipo> getTipos() {
        return tipoDao.loadAll();
    }
}

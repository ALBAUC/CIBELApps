package es.unican.cibelapps.activities.activos;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import es.unican.cibelapps.repository.db.VulnerabilidadDao;

public class CatalogoPresenter implements ICatalogoContract.Presenter {

    private final ICatalogoContract.View view;
    private ActivoDao activoDao;
    private TipoDao tipoDao;
    private PerfilDao perfilDao;
    private VulnerabilidadDao vulnerabilidadDao;
    private ICibelRepository cibelRepository;
    private Perfil perfil;
    private List<String> appsNoDatos;

    public CatalogoPresenter(ICatalogoContract.View view) {
        this.view = view;
        appsNoDatos = new ArrayList<>();
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
        vulnerabilidadDao = daoSession.getVulnerabilidadDao();

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
        List<PackageInfo> appsInstaladas = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        for (PackageInfo packageInfo : appsInstaladas) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String nombreAppEnDispositivo = packageInfo.applicationInfo.loadLabel(pm).toString();
                String versionAppEnDispositivo = packageInfo.versionName;

                Activo mejorCoincidencia = encontrarCoincidenciaPorNombre(nombreAppEnDispositivo);
                if (mejorCoincidencia != null) {
                    // Filtrar vulnerabilidades por version
                    for (Vulnerabilidad v : mejorCoincidencia.getVulnerabilidades()) {
                        String versionEndExcluding = v.getVersionEndExcluding();
                        String versionEndIncluding = v.getVersionEndIncluding();
                        if (!((versionEndExcluding == null || compareVersions(versionAppEnDispositivo, versionEndExcluding) < 0)
                                && (versionEndIncluding == null || compareVersions(versionAppEnDispositivo, versionEndIncluding) <= 0))) {
                            // La aplicación no está afectada por la vulnerabilidad
                            v.setAfectaApp(false);
                        } else {
                            v.setAfectaApp(true);
                        }
                        vulnerabilidadDao.update(v);
                    }
                    // Añadir activo al perfil
                    mejorCoincidencia.setFk_perfil(perfil.getId());
                    perfil.getActivosAnhadidos().add(mejorCoincidencia);
                    activoDao.update(mejorCoincidencia);
                    perfilDao.update(perfil);

                    appsCargadas.add(mejorCoincidencia);
                } else {
                    appsNoDatos.add(nombreAppEnDispositivo);
                }
            }
        }
        return appsCargadas;
    }

    // Función para comparar versiones numéricamente
    private static int compareVersions(String version1, String version2) {
        String numericPart1 = extractNumericPart(version1);
        String numericPart2 = extractNumericPart(version2);

        if (numericPart1 != null && numericPart2 != null) {
            String[] parts1 = numericPart1.split("\\.");
            String[] parts2 = numericPart2.split("\\.");

            for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
                int part1 = Integer.parseInt(parts1[i]);
                int part2 = Integer.parseInt(parts2[i]);

                if (part1 < part2) {
                    return -1;
                } else if (part1 > part2) {
                    return 1;
                }
            }

            return Integer.compare(parts1.length, parts2.length);
        }

        // Manejar el caso en que no se pudo extraer la parte numérica de alguna de las versiones
        return -1;
    }

    private static String extractNumericPart(String version) {
        // Patrón para buscar una cadena que comience con dígitos y un punto (versiones entre 2 y 4 partes)
        Pattern pattern = Pattern.compile("(\\d+\\.\\d+(\\.\\d+){0,2})");
        Matcher matcher = pattern.matcher(version);

        // Buscar la coincidencia en la cadena
        if (matcher.find()) {
            return matcher.group(1);  // Devolver el grupo coincidente
        } else {
            return null;  // No se encontró un formato de versión válido
        }
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

    public List<String> getNoDatosApps() {
        return appsNoDatos;
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

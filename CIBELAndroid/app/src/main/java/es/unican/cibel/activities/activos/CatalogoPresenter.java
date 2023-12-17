package es.unican.cibel.activities.activos;

import android.database.sqlite.SQLiteException;

import java.util.List;

import es.unican.cibel.common.Callback;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Perfil;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.model.Vulnerabilidad;
import es.unican.cibel.repository.cibel.CibelRepository;
import es.unican.cibel.repository.cibel.ICibelRepository;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.PerfilDao;
import es.unican.cibel.repository.db.TipoDao;

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

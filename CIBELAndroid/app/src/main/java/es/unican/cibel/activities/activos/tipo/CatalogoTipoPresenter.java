package es.unican.cibel.activities.activos.tipo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Perfil;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.PerfilDao;

public class CatalogoTipoPresenter implements ICatalogoTipoContract.Presenter {

    private final ICatalogoTipoContract.View view;
    private final Tipo tipo;
    private ActivoDao activoDao;
    private PerfilDao perfilDao;

    public CatalogoTipoPresenter(Tipo tipo, ICatalogoTipoContract.View view) {
        this.view = view;
        this.tipo = tipo;
    }

    @Override
    public void init() {
        DaoSession daoSession = view.getMyApplication().getDaoSession();
        activoDao = daoSession.getActivoDao();
        perfilDao = daoSession.getPerfilDao();
    }

    @Override
    public String getTipoName() {
        return tipo.getNombre();
    }

    @Override
    public List<Activo> getActivosDeTipo() {
        List<Activo> activosDeTipo = activoDao.queryBuilder().where(ActivoDao.Properties.Fk_tipo
                .like(tipo.getIdTipo().toString())).list();
        return activosDeTipo;
    }

    @Override
    public List<Activo> getActivosTipoOrdenadosPorSeguridadDesc() {
        List<Activo> activosDeTipo = getActivosDeTipo();
        Collections.sort(activosDeTipo, new Comparator<Activo>() {
            @Override
            public int compare(Activo activo1, Activo activo2) {
                int gravedad1 = (int) Math.round(activo1.calcularTotalGravedad());
                int gravedad2 = (int) Math.round(activo2.calcularTotalGravedad());
                return Integer.compare(gravedad1, gravedad2);
            }
        });

        return activosDeTipo;
    }

    @Override
    public List<Activo> getActivosTipoOrdenadosPorSeguridadAsc() {
        List<Activo> activosDeTipo = getActivosDeTipo();
        Collections.sort(activosDeTipo, new Comparator<Activo>() {
            @Override
            public int compare(Activo activo1, Activo activo2) {
                int gravedad1 = (int) Math.round(activo1.calcularTotalGravedad());
                int gravedad2 = (int) Math.round(activo2.calcularTotalGravedad());
                return Integer.compare(gravedad2, gravedad1);
            }
        });

        return activosDeTipo;
    }

    @Override
    public List<Activo> getActivosPerfil() {
        Perfil perfil = Perfil.getInstance(perfilDao);
        List<Activo> perfilAssets = activoDao._queryPerfil_ActivosAnhadidos(perfil.getId());
        return perfilAssets;
    }

    @Override
    public Activo getAssetByName(String appName) {
        return activoDao.queryBuilder().where(ActivoDao.Properties.Nombre.like(appName)).unique();
    }
}

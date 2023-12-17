package es.unican.cibel.activities.activos.search;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Perfil;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.PerfilDao;
import es.unican.cibel.repository.db.TipoDao;

public class SearchResultPresenter implements ISearchResultContract.Presenter {

    private final ISearchResultContract.View view;
    private ActivoDao activoDao;
    private TipoDao tipoDao;
    private PerfilDao perfilDao;

    public SearchResultPresenter(ISearchResultContract.View view) {
        this.view = view;
    }

    @Override
    public void init() {
        DaoSession daoSession = view.getMyApplication().getDaoSession();
        activoDao = daoSession.getActivoDao();
        tipoDao = daoSession.getTipoDao();
        perfilDao = daoSession.getPerfilDao();
    }

    @Override
    public List<Activo> doSearch(String query) {
        String modifiedQuery = "%" + removeAccents(query.trim().toLowerCase()) + "%";

        List<Tipo> tipos = tipoDao.loadAll();
        List<Long> tipoIds = new ArrayList<>();
        for (Tipo c : tipos) {
            String nombre = removeAccents(c.getNombre().trim().toLowerCase());
            if (nombre.contains(removeAccents(query.trim().toLowerCase()))) {
                tipoIds.add(c.getIdTipo());
            }
        }

        return activoDao.queryBuilder()
                .whereOr(
                        ActivoDao.Properties.Nombre.like(modifiedQuery),
                        ActivoDao.Properties.Fk_tipo.in(tipoIds)
                ).list();
    }

    @Override
    public Activo getAssetByName(String appName) {
        return activoDao.queryBuilder().where(ActivoDao.Properties.Nombre.like(appName)).unique();
    }

    @Override
    public List<Activo> getPerfilAssets() {
        Perfil perfil = Perfil.getInstance(perfilDao);
        List<Activo> perfilAssets = activoDao._queryPerfil_ActivosAnhadidos(perfil.getId());
        return perfilAssets;
    }

    @Override
    public List<Activo> getActivosOrdenadosPorSeguridadDesc(String query) {
        List<Activo> activos = doSearch(query);
        Collections.sort(activos, new Comparator<Activo>() {
            @Override
            public int compare(Activo activo1, Activo activo2) {
                int gravedad1 = (int) Math.round(activo1.calcularTotalGravedad());
                int gravedad2 = (int) Math.round(activo2.calcularTotalGravedad());
                return Integer.compare(gravedad1, gravedad2);
            }
        });

        return activos;
    }

    @Override
    public List<Activo> getActivosOrdenadosPorSeguridadAsc(String query) {
        List<Activo> activos = doSearch(query);
        Collections.sort(activos, new Comparator<Activo>() {
            @Override
            public int compare(Activo activo1, Activo activo2) {
                int gravedad1 = (int) Math.round(activo1.calcularTotalGravedad());
                int gravedad2 = (int) Math.round(activo2.calcularTotalGravedad());
                return Integer.compare(gravedad2, gravedad1);
            }
        });

        return activos;
    }

    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}

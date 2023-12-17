package es.unican.cibel.activities.activos.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.TipoDao;

public class AppSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "es.unican.cibel.activities.activos.search.AppSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    private ActivoDao activoDao;
    private TipoDao tipoDao;

    public AppSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query = uri.getLastPathSegment();

        MatrixCursor cursor = new MatrixCursor(new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_ICON_1
        });

        DaoSession daoSession = ((MyApplication) getContext().getApplicationContext()).getDaoSession();
        activoDao = daoSession.getActivoDao();
        tipoDao = daoSession.getTipoDao();

        List<Activo> assetsSuggestions = new ArrayList<>();
        if (query != null){
            assetsSuggestions = getassetsSuggestions(query);
        }

        // Agregar las sugerencias al cursor
        int id = 0;
        for (Activo a : assetsSuggestions) {
            String assetName = a.getNombre();
            String tipoName = a.getTipo().getNombre();
            String intentData = "app://" + assetName;
            Bitmap iconBitmap = downloadIconBitmap(a.getIcono());
            cursor.addRow(new Object[]{
                    id++,
                    assetName,
                    tipoName,
                    intentData,
                    getBitmapUri(getContext(), iconBitmap, assetName)
            });
        }

        return cursor;
    }

    private List<Activo> getassetsSuggestions(String query) {
        String modifiedQuery = removeAccents(query.trim().toLowerCase());

        List<Tipo> tipos = tipoDao.loadAll();
        List<Long> tipoIds = new ArrayList<>();
        for (Tipo c : tipos) {
            String nombre = removeAccents(c.getNombre().trim().toLowerCase());
            if (nombre.contains(modifiedQuery)) {
                tipoIds.add(c.getIdTipo());
            }
        }

        List<Activo> activos = activoDao.queryBuilder()
                .whereOr(
                        ActivoDao.Properties.Nombre.like("%" + modifiedQuery + "%"),
                        ActivoDao.Properties.Fk_tipo.in(tipoIds)
                ).list();

        return activos;
    }

    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private Bitmap downloadIconBitmap(String imageUrl) {
        try {
            return Picasso.get().load(imageUrl)
                    .resize(600, 600)
                    .centerCrop()
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getBitmapUri(Context context, Bitmap bitmap, String appName) {
        if (bitmap != null) {
            try {
                File cachePath = new File(context.getCacheDir(), "images");
                cachePath.mkdirs();

                // Generar un nombre de archivo único basado en el nombre de la aplicación
                String fileName = appName + "_" + System.currentTimeMillis() + ".jpeg";

                FileOutputStream stream = new FileOutputStream(cachePath + File.separator + fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();

                return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(cachePath, fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
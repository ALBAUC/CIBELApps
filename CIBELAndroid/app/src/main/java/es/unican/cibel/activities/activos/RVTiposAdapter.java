package es.unican.cibel.activities.activos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unican.cibel.R;
import es.unican.cibel.activities.activos.tipo.CatalogoTipoView;
import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;

/**
 * Crea y actualiza las vistas que representan cada fila de la lista de categorias.
 */
public class RVTiposAdapter extends RecyclerView.Adapter<RVTiposAdapter.TipoViewHolder> {
    private Context context;
    private final List<Tipo> tipos;
    private final List<Activo> perfilActivos;
    private final LayoutInflater inflater;
    private ActivoDao activoDao;
    private MyApplication myApplication;

    public RVTiposAdapter(Context context, List<Tipo> tipos, List<Activo> perfilActivos, MyApplication myApplication) {
        this.tipos = tipos;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.perfilActivos = perfilActivos;
        DaoSession daoSession = myApplication.getDaoSession();
        activoDao = daoSession.getActivoDao();
        this.myApplication = myApplication;
    }

    @NonNull
    @Override
    public TipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_catalogo_categoria, parent, false);
        return new TipoViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return tipos.size();
    }

    @Override
    public void onBindViewHolder(TipoViewHolder holder, int position) {
        holder.tipo = tipos.get(position);
        List<Activo> activos = activoDao.queryBuilder().where(ActivoDao.Properties.Fk_tipo
                .like(tipos.get(position).getIdTipo().toString())).list();
        holder.activosRV.setAdapter(new RVAssetsAdapter(context, activos, perfilActivos, myApplication));
        holder.activosRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.activosRV.setHasFixedSize(true);
        holder.tipoNameTV.setText(tipos.get(position).getNombre());
    }

    public class TipoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tipoNameTV;
        private final RecyclerView activosRV;
        private Tipo tipo;

        public TipoViewHolder(View itemView) {
            super(itemView);
            tipoNameTV = itemView.findViewById(R.id.categoriaName_tv);
            activosRV = itemView.findViewById(R.id.apps_rv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.container))
                    .add(R.id.container, CatalogoTipoView.newInstance(tipo))
                    .setReorderingAllowed(true)
                    .addToBackStack("tipos")
                    .commit();
        }
    }
}

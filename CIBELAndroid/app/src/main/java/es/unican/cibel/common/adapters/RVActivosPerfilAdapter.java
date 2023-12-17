package es.unican.cibel.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.unican.cibel.R;
import es.unican.cibel.activities.activos.detail.AssetDetailView;
import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.VulnerabilidadDao;

public class RVActivosPerfilAdapter extends RecyclerView.Adapter<RVActivosPerfilAdapter.AssetViewHolder> {

    private Context context;
    private final List<Activo> activos;
    private final List<Activo> perfilActivos;
    private final LayoutInflater inflater;
    private VulnerabilidadDao vulnerabilidadDao;

    // Para pestaña perfil
    public RVActivosPerfilAdapter(Context context, List<Activo> activos, MyApplication myApplication) {
        this.activos = activos;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.perfilActivos = null;
        DaoSession daoSession = myApplication.getDaoSession();
        vulnerabilidadDao = daoSession.getVulnerabilidadDao();
    }

    // Para buscador
    public RVActivosPerfilAdapter(Context context, List<Activo> activos, List<Activo> perfilActivos, MyApplication myApplication) {
        this.activos = activos;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.perfilActivos = perfilActivos;
        DaoSession daoSession = myApplication.getDaoSession();
        vulnerabilidadDao = daoSession.getVulnerabilidadDao();
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_perfil_asset, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {
        Activo activo = activos.get(position);
        holder.activo = activo;

        // Nombre activo
        holder.assetNameTV.setText(activo.getNombre());

        // Seguridad
        int puntuacionSeguridad = activo.calcularPuntuacionSeguridad();
        holder.assetSecurityTV.setText(activo.getEtiquetaSecurityFromTramo(puntuacionSeguridad));
        holder.assetSecurityIconIV.setColorFilter(ContextCompat.getColor(context, activo.getColorFromTramo(puntuacionSeguridad)));

        // Foto activo
        Picasso.get().load(activo.getIcono())
                .resize(600, 600)
                .centerCrop()
                .into(holder.assetIconIV);

        // Icono añadido buscador
        if (perfilActivos != null && perfilActivos.contains(activo)) {
            holder.addedIconIV.setVisibility(View.VISIBLE);
        } else {
            holder.addedIconIV.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return activos.size();
    }

    public void updateElementsList(List<Activo> updatedElements) {
        activos.clear();
        activos.addAll(updatedElements);
        notifyDataSetChanged();
    }

    public class AssetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView assetNameTV;
        private final ImageView assetIconIV;
        private final TextView assetSecurityTV;
        private final ImageView addedIconIV;
        private final ImageView assetSecurityIconIV;
        private Activo activo;

        public AssetViewHolder(@NonNull View itemView) {
            super(itemView);
            assetNameTV = itemView.findViewById(R.id.appAddedName_tv);
            assetIconIV = itemView.findViewById(R.id.appAddedIcon_iv);
            assetSecurityTV = itemView.findViewById(R.id.assetAddedVuln_tv);
            addedIconIV = itemView.findViewById(R.id.infoAddedIcon_iv);
            assetSecurityIconIV = itemView.findViewById(R.id.securityIcon_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.container))
                    .add(R.id.container, AssetDetailView.newInstance(activo))
                    .setReorderingAllowed(true)
                    .addToBackStack("perfil")
                    .commit();
        }
    }
}

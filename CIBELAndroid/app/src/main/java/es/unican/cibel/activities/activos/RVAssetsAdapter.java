package es.unican.cibel.activities.activos;

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
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.DaoSession;

public class RVAssetsAdapter extends RecyclerView.Adapter<RVAssetsAdapter.AssetViewHolder> {
    private Context context;
    private final List<Activo> activos;
    private final List<Activo> perfilActivos;
    private final LayoutInflater inflater;
    private ActivoDao activoDao;

    public RVAssetsAdapter(Context context, List<Activo> data, List<Activo> perfilActivos, MyApplication myApplication) {
        this.context = context;
        this.activos = data;
        this.perfilActivos = perfilActivos;
        this.inflater = LayoutInflater.from(context);

        DaoSession daoSession = myApplication.getDaoSession();
        activoDao = daoSession.getActivoDao();
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_catalogo_asset, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return activos.size();
    }

    @Override
    public void onBindViewHolder(AssetViewHolder holder, int position) {
        Activo activo = activos.get(position);
        //Log.d("RVAssetsAdapter", activo.toString());
        holder.activo = activo;
        holder.assetName_tv.setText(activo.getNombre());
        Picasso.get().load(activo.getIcono())
                .resize(600, 600)
                .centerCrop()
                .into(holder.assetIcon_iv);
        if (perfilActivos.contains(activo)) {
            holder.assetAddedIcon_iv.setVisibility(View.VISIBLE);
        } else {
            holder.assetAddedIcon_iv.setVisibility(View.GONE);
        }

        holder.assetSecurityIV.setColorFilter(ContextCompat.getColor(context, activo.getColorFromTramo(activo.calcularPuntuacionSeguridad())));
        holder.assetSecurityTV.setText(String.valueOf(activo.calcularPuntuacionSeguridad()));
    }

    public class AssetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView assetIcon_iv;
        private final TextView assetName_tv;
        private final ImageView assetAddedIcon_iv;
        private final ImageView assetSecurityIV;
        private final TextView assetSecurityTV;
        private Activo activo;

        public AssetViewHolder(View itemView) {
            super(itemView);
            assetName_tv = itemView.findViewById(R.id.appName_tv);
            assetIcon_iv = itemView.findViewById(R.id.appIcon_iv);
            assetAddedIcon_iv = itemView.findViewById(R.id.appAddedIcon_iv);
            assetSecurityIV = itemView.findViewById(R.id.securityIcon_iv);
            assetSecurityTV = itemView.findViewById(R.id.security_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.container))
                    .add(R.id.container, AssetDetailView.newInstance(activo))
                    .setReorderingAllowed(true)
                    .addToBackStack("apps")
                    .commit();
        }
    }
}

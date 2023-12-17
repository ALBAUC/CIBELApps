package es.unican.cibel.activities.activos.detail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import es.unican.cibel.R;
import es.unican.cibel.activities.activos.detail.cve.CveDetailView;
import es.unican.cibel.model.Vulnerabilidad;

public class RVCvesAdapter extends RecyclerView.Adapter<RVCvesAdapter.CveViewHolder> {

    private Context context;
    private final LayoutInflater inflater;
    private final List<Vulnerabilidad> vulnerabilidades;

    public RVCvesAdapter(Context context, List<Vulnerabilidad> vulnerabilidades) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.vulnerabilidades = vulnerabilidades;
    }

    @NonNull
    @Override
    public CveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_assets_cve, parent, false);
        return new CveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVCvesAdapter.CveViewHolder holder, int position) {
        Vulnerabilidad vulnerabilidad = vulnerabilidades.get(position);
        holder.vulnerabilidad = vulnerabilidad;
        holder.cveIdTV.setText(vulnerabilidad.getIdCVE());
        holder.cveSeverityIV.setColorFilter(ContextCompat.getColor(context, vulnerabilidad.getColorFromSeverity()));

        holder.impactoHBC.setDrawBarShadow(false);
        Description description = new Description();
        description.setText("");
        holder.impactoHBC.setDescription(description);
        holder.impactoHBC.getLegend().setEnabled(false);
        holder.impactoHBC.setPinchZoom(false);
        holder.impactoHBC.setDrawValueAboveBar(false);
        holder.impactoHBC.setTouchEnabled(false);
        holder.impactoHBC.setDragEnabled(false);
        holder.impactoHBC.setScaleEnabled(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        String confImpact = vulnerabilidad.getConfidentialityImpact();
        String integImpact = vulnerabilidad.getIntegrityImpact();
        String availImpact = vulnerabilidad.getAvailabilityImpact();
        int mappedConf = vulnerabilidad.mapImpact(confImpact);
        int mappedInteg = vulnerabilidad.mapImpact(integImpact);
        int mappedAvail = vulnerabilidad.mapImpact(availImpact);
        entries.add(new BarEntry(0f, mappedAvail));
        entries.add(new BarEntry(1f, mappedInteg));
        entries.add(new BarEntry(2f, mappedConf));

        BarDataSet barDataSet = new BarDataSet(entries, "Bar Data Set");
        barDataSet.setColors(
                ContextCompat.getColor(context, vulnerabilidad.getColorFromImpact(availImpact)),
                ContextCompat.getColor(context, vulnerabilidad.getColorFromImpact(integImpact)),
                ContextCompat.getColor(context, vulnerabilidad.getColorFromImpact(confImpact))
        );
        barDataSet.setDrawValues(false);

        XAxis xAxis = holder.impactoHBC.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        String[] etiquetas = {"Disponibilidad", "Integridad", "Confidencialidad"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(etiquetas));
        xAxis.setLabelCount(3);
        xAxis.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_light));

        YAxis yLeft = holder.impactoHBC.getAxisLeft();
        yLeft.setAxisMaximum(4f);
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);

        YAxis yRight = holder.impactoHBC.getAxisRight();
        yRight.setEnabled(false);

        holder.impactoHBC.setDrawBarShadow(true);
        barDataSet.setBarShadowColor(Color.argb(40, 150, 150, 150));
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        holder.impactoHBC.setData(data);
        holder.impactoHBC.invalidate();
    }

    @Override
    public int getItemCount() {
        return vulnerabilidades.size();
    }

    public class CveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView cveIdTV;
        private final ImageView cveSeverityIV;
        private Vulnerabilidad vulnerabilidad;
        private final HorizontalBarChart impactoHBC;

        public CveViewHolder(@NonNull View itemView) {
            super(itemView);
            cveIdTV = itemView.findViewById(R.id.cveId_tv);
            cveSeverityIV = itemView.findViewById(R.id.severityIcon_iv);
            impactoHBC = itemView.findViewById(R.id.impacto_hbc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.container))
                    .add(R.id.container, CveDetailView.newInstance(vulnerabilidad))
                    .setReorderingAllowed(true)
                    .addToBackStack("cves")
                    .commit();
        }
    }
}

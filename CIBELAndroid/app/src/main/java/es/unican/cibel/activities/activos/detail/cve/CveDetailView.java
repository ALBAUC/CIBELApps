package es.unican.cibel.activities.activos.detail.cve;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import es.unican.cibel.R;
import es.unican.cibel.activities.main.MainView;
import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Vulnerabilidad;

public class CveDetailView extends Fragment implements  ICveDetailContract.View, MainView.RefreshableFragment {

    public static final String FRAGMENT_CVE = "cve";
    private ICveDetailContract.Presenter presenter;
    private HorizontalBarChart baseScoreHBC;
    private Vulnerabilidad vulnerabilidad;

    public static CveDetailView newInstance(Vulnerabilidad vulnerabilidad) {
        CveDetailView fragment = new CveDetailView();
        Bundle args = new Bundle();
        args.putParcelable(FRAGMENT_CVE, vulnerabilidad);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cve_detail, container, false);

        Bundle args = getArguments();
        if (args != null) {
            // Obtener la vulnerabilidad del fragmento que lanzo este frgamento
            vulnerabilidad = args.getParcelable(FRAGMENT_CVE);
            presenter = new CveDetailPresenter(vulnerabilidad, this);
            presenter.init();

            // Link a los elementos de la vista
            ImageView severityIconIV = layout.findViewById(R.id.severityIcon_iv);
            TextView cveIdTV = layout.findViewById(R.id.cveId_tv);
            TextView descripcionTV = layout.findViewById(R.id.descricion_tv);
            ImageView infoImpactoIV = layout.findViewById(R.id.infoImpacto_iv);

            TextView baseScoreTV = layout.findViewById(R.id.baseScore_tv);
            baseScoreHBC = layout.findViewById(R.id.baseScore_hbc);

            TextView condifencialidadTV = layout.findViewById(R.id.confidencialidad_tv);
            HorizontalBarChart confidencialidadHBC = layout.findViewById(R.id.confidencialidad_hbc);

            TextView integridadTV = layout.findViewById(R.id.integridad_tv);
            HorizontalBarChart integridadHBC = layout.findViewById(R.id.integridad_hbc);

            TextView disponibilidadTV = layout.findViewById(R.id.disponibilidad_tv);
            HorizontalBarChart disponibilidadHBC = layout.findViewById(R.id.disponibilidad_hbc);

            // Asignar valores
            severityIconIV.setColorFilter(ContextCompat.getColor(getContext(), vulnerabilidad.getColorFromSeverity()));
            cveIdTV.setText(presenter.getCveId());
            descripcionTV.setText(presenter.getCveDescripcion());

            infoImpactoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Información sobre Impacto");

                    // Añade el mensaje informativo
                    builder.setMessage(Html.fromHtml(getResources().getString(R.string.cve_info_impacto)));

                    // Añade un botón de cierre
                    builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cierra el diálogo
                            dialog.dismiss();
                        }
                    });

                    // Muestra el diálogo
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            baseScoreTV.setText(String.valueOf(presenter.getCveBaseScore()));
            baseScoreTV.setTextColor(ContextCompat.getColor(getContext(), vulnerabilidad.getColorFromSeverity()));
            setBaseScoreGraph();

            String confidenciality = presenter.getCveConfidenciality();
            condifencialidadTV.setText(getTranslationForImpact(confidenciality));
            condifencialidadTV.setTextColor(ContextCompat.getColor(getContext(),
                    vulnerabilidad.getColorFromImpact(confidenciality)));
            setGraph(confidencialidadHBC, confidenciality);

            String integrity = presenter.getCveIntegrity();
            integridadTV.setText(getTranslationForImpact(integrity));
            integridadTV.setTextColor(ContextCompat.getColor(getContext(),
                    vulnerabilidad.getColorFromImpact(integrity)));
            setGraph(integridadHBC, integrity);

            String availability = presenter.getCveAvailability();
            disponibilidadTV.setText(getTranslationForImpact(availability));
            disponibilidadTV.setTextColor(ContextCompat.getColor(getContext(),
                    vulnerabilidad.getColorFromImpact(availability)));
            setGraph(disponibilidadHBC, availability);
        }

        return layout;
    }
    private String getTranslationForImpact(String impact) {
        switch (impact) {
            case Vulnerabilidad.IMPACT_N:
                return Vulnerabilidad.ES_IMPACT_N;
            case Vulnerabilidad.IMPACT_L:
                return Vulnerabilidad.ES_IMPACT_L;
            case Vulnerabilidad.IMPACT_P:
                return Vulnerabilidad.ES_IMPACT_P;
            case Vulnerabilidad.IMPACT_H:
                return Vulnerabilidad.ES_IMPACT_H;
            case Vulnerabilidad.IMPACT_C:
                return Vulnerabilidad.ES_IMPACT_C;
            default:
                return impact;
        }
    }

    private void setBaseScoreGraph() {
        baseScoreHBC.setDrawBarShadow(false);
        Description description = new Description();
        description.setText("");
        baseScoreHBC.setDescription(description);
        baseScoreHBC.getLegend().setEnabled(false);
        baseScoreHBC.setPinchZoom(false);
        baseScoreHBC.setDrawValueAboveBar(false);
        baseScoreHBC.setTouchEnabled(false);
        baseScoreHBC.setDragEnabled(false);
        baseScoreHBC.setScaleEnabled(false);

        XAxis xAxis = baseScoreHBC.getXAxis();
        xAxis.setEnabled(false);

        YAxis yLeft = baseScoreHBC.getAxisLeft();
        yLeft.setAxisMaximum(10f);
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);

        YAxis yRight = baseScoreHBC.getAxisRight();
        yRight.setEnabled(false);

        setGraphData();
    }

    private void setGraphData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) presenter.getCveBaseScore()));

        BarDataSet barDataSet = new BarDataSet(entries, "Bar Data Set");
        barDataSet.setColor(ContextCompat.getColor(getContext(), vulnerabilidad.getColorFromSeverity()));
        barDataSet.setDrawValues(false);
        baseScoreHBC.setDrawBarShadow(true);
        barDataSet.setBarShadowColor(Color.argb(40, 150, 150, 150));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(1f);

        baseScoreHBC.setData(data);
        baseScoreHBC.invalidate();
    }

    private void setGraph(HorizontalBarChart chart, String impact) {
        chart.setDrawBarShadow(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.getLegend().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawValueAboveBar(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yLeft = chart.getAxisLeft();
        yLeft.setAxisMaximum(4f); // Porque tienes 5 valores discretos
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);

        YAxis yRight = chart.getAxisRight();
        yRight.setEnabled(false);

        setGraphData(chart, impact);
    }

    private void setGraphData(HorizontalBarChart chart, String impact) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        // Aquí mapea tus valores discretos a un rango de 0 a 4
        // Ejemplo: Si impact es "NONE", asigna 0. Si es "LOW", asigna 1, y así sucesivamente.
        int mappedValue = presenter.mapImpact(impact);

        entries.add(new BarEntry(0f, mappedValue));

        BarDataSet barDataSet = new BarDataSet(entries, "Bar Data Set");
        // Asigna un color según el valor
        barDataSet.setColor(ContextCompat.getColor(getContext(), vulnerabilidad.getColorFromImpact(impact)));
        barDataSet.setDrawValues(false);

        chart.setDrawBarShadow(true);
        barDataSet.setBarShadowColor(Color.argb(40, 150, 150, 150));
        BarData data = new BarData(barDataSet);

        data.setBarWidth(1f);

        chart.setData(data);
        chart.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requireActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public MyApplication getMyApplication() {
        return (MyApplication) requireActivity().getApplication();
    }

    @Override
    public void refreshData() {
        // No hay nada que actualizar
    }
}

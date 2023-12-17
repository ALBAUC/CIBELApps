package es.unican.cibel.activities.activos.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.unican.cibel.R;
import es.unican.cibel.activities.activos.detail.AssetDetailView;
import es.unican.cibel.activities.main.MainView;
import es.unican.cibel.common.adapters.RVActivosPerfilAdapter;
import es.unican.cibel.common.MyApplication;
import es.unican.cibel.model.Activo;


public class SearchResultView extends Fragment implements ISearchResultContract.View, MainView.RefreshableFragment {

    private static final String PREF_ASSETS_BUSCADOR_SORT_ORDER = "assets_buscador_sort_order";
    public static final String QUERY = "query";
    private ISearchResultContract.Presenter presenter;
    private TextView sortInfoTV;
    private View overlayView;
    private RecyclerView assetsRV;
    private SharedPreferences prefs;
    private String query;

    public static SearchResultView newInstance(String query) {
        SearchResultView fragment = new SearchResultView();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchResultPresenter(this);
        presenter.init();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);

        Bundle args = getArguments();
        if (args != null) {
            // Link a los elemntos de la vista
            assetsRV = layout.findViewById(R.id.appsResultSearch_rv);
            LinearLayout sortLL = layout.findViewById(R.id.sort_ll);
            sortInfoTV = layout.findViewById(R.id.sortInfo_tv);

            query = args.getString(QUERY);
            requireActivity().setTitle(query);

            // Configurar lista de dispositivos
            assetsRV.setLayoutManager(new LinearLayoutManager(getContext()));
            DividerItemDecoration dividerA = new DividerItemDecoration(
                    assetsRV.getContext(),
                    DividerItemDecoration.VERTICAL);
            assetsRV.addItemDecoration(dividerA);

            // Ordenar dispositivos
            prefs = requireContext().getSharedPreferences("SortAssetsPrefs", Context.MODE_PRIVATE);
            String sortOrder = prefs.getString(PREF_ASSETS_BUSCADOR_SORT_ORDER, getResources().getString(R.string.ordenarSeguridad_Asc));
            sortInfoTV.setText(sortOrder);
            updateRecyclerView(sortOrder);

            sortLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSortingOptionsPopup();
                }
            });
        }
        return layout;
    }

    private void updateRecyclerView(String sortOrder) {
        if (sortOrder.equals(getResources().getString(R.string.ordenarSeguridad_Desc))) {
            assetsRV.setAdapter(new RVActivosPerfilAdapter(getContext(), presenter.getActivosOrdenadosPorSeguridadDesc(query), presenter.getPerfilAssets(), getMyApplication()));
        } else {
            assetsRV.setAdapter(new RVActivosPerfilAdapter(getContext(), presenter.getActivosOrdenadosPorSeguridadAsc(query), presenter.getPerfilAssets(), getMyApplication()));
        }
    }

    private void showSortingOptionsPopup() {
        // Mostrar opciones de ordenacion
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.sorting_options_assets, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, // Ancho completo
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        setUpSortingOptionsClickListener(popupView, popupWindow);

        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

        // Destacar opcion de ordenacion actual
        ImageView seguridadAscIV = popupView.findViewById(R.id.seguridadAsc_iv);
        ImageView seguridadDescIV = popupView.findViewById(R.id.seguridadDesc_iv);
        TextView seguridadAscTV = popupView.findViewById(R.id.seguridadAsc_tv);
        TextView seguridadDescTV = popupView.findViewById(R.id.seguridadDesc_tv);

        if (sortInfoTV.getText().equals(getResources().getString(R.string.ordenarSeguridad_Asc))) {
            seguridadAscIV.setVisibility(View.VISIBLE);
            seguridadAscTV.setTypeface(Typeface.DEFAULT_BOLD);
            seguridadAscTV.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
        } else if (sortInfoTV.getText().equals(getResources().getString(R.string.ordenarSeguridad_Desc))) {
            seguridadDescIV.setVisibility(View.VISIBLE);
            seguridadDescTV.setTypeface(Typeface.DEFAULT_BOLD);
            seguridadDescTV.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
        }

        // Difuminar resto de la interfaz
        overlayView = new View(getContext());
        overlayView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_background));
        int overlayAlpha = 150; // Valor entre 0 (transparente) y 255 (opaco)
        overlayView.getBackground().setAlpha(overlayAlpha);
        ViewGroup mainLayout = getActivity().findViewById(android.R.id.content);
        mainLayout.addView(overlayView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (overlayView != null) {
                    mainLayout.removeView(overlayView);
                    overlayView = null;
                }
            }
        });
    }

    private void setUpSortingOptionsClickListener(View popupView, PopupWindow popupWindow) {
        // Configurar OnClickListener para cada opción
        LinearLayout linearLayout = popupView.findViewById(R.id.ordenar_activos_ll);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childView = linearLayout.getChildAt(i);
            if (childView instanceof LinearLayout) {
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int checkedId = view.getId();
                        sortAssets(checkedId);
                        popupWindow.dismiss();
                    }
                });
            }
        }
    }

    private void sortAssets(int checkedId) {
        // Actualizar la opción de ordenación
        String sortOrder = getSortOrderFromCheckedId(checkedId);
        sortInfoTV.setText(sortOrder);
        prefs.edit().putString(PREF_ASSETS_BUSCADOR_SORT_ORDER, sortOrder).apply();

        // Actualizar el RecyclerView según la opción de ordenación
        updateRecyclerView(sortOrder);
    }

    private String getSortOrderFromCheckedId(int checkedId) {
        String sortOrder = getResources().getString(R.string.ordenarSeguridad_Asc);
        switch (checkedId) {
            case R.id.seguridadAsc_ll:
                sortOrder = getResources().getString(R.string.ordenarSeguridad_Asc);
                break;
            case R.id.seguridadDesc_ll:
                sortOrder = getResources().getString(R.string.ordenarSeguridad_Desc);
                break;
        }
        return sortOrder;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtén el intent y verifica la acción
        Intent intent = requireActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            presenter.doSearch(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Maneja el clic en una sugerencia (porque todas las sugerencias usan ACTION_VIEW)
            Uri data = intent.getData();
            showResult(data);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requireActivity().getSupportFragmentManager().popBackStack();
                requireActivity().setTitle(R.string.bottom_nav_home);
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

    }

    private void showResult(Uri data) {
        if (data != null) {
            String intentData = data.toString();
            if (intentData.startsWith("app://")) {
                String assetName = intentData.substring(6);
                Activo activo = presenter.getAssetByName(assetName);
                if (activo != null) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, AssetDetailView.newInstance(activo))
                            .setReorderingAllowed(true)
                            .addToBackStack("apps")
                            .commit();
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View rootView = requireView();
                    if (rootView != null) {
                        inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    }
                }
            }
        }
    }
}


package es.unican.cibel.activities.activos.tipo;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.unican.cibel.R;
import es.unican.cibel.activities.activos.detail.AssetDetailView;
import es.unican.cibel.activities.activos.search.SearchResultView;
import es.unican.cibel.activities.main.MainView;
import es.unican.cibel.common.MyApplication;
import es.unican.cibel.common.adapters.RVActivosPerfilAdapter;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Tipo;

public class CatalogoTipoView extends Fragment implements ICatalogoTipoContract.View, MainView.RefreshableFragment {

    private static final String PREF_ASSETS_CATALOGO_SORT_ORDER = "assets_catalogo_sort_order";
    public static final String FRAGMENT_TIPO = "tipo";
    private ICatalogoTipoContract.Presenter presenter;
    private TextView sortInfoTV;
    private View overlayView;
    private RecyclerView assetsTipoRV;
    private SharedPreferences prefs;

    public static CatalogoTipoView newInstance(Tipo tipo) {
        CatalogoTipoView fragment = new CatalogoTipoView();
        Bundle args = new Bundle();
        args.putParcelable(FRAGMENT_TIPO, tipo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = requireActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Manejar el resultado de la búsqueda según tus necesidades
            showSearchResultView(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Manejar el clic en una sugerencia
            Uri data = intent.getData();
            showAppDetailViewSuggestion(data);
        }
    }

    private void showSearchResultView(String query) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SearchResultView.newInstance(query))
                .setReorderingAllowed(true)
                .addToBackStack("categoria")
                .commit();
        // Eliminar los intent anteriores
        requireActivity().setIntent(new Intent());
    }

    private void showAppDetailViewSuggestion(Uri data) {
        if (data != null) {
            String intentData = data.toString();
            if (intentData.startsWith("app://")) {
                String appName = intentData.substring(6);
                Activo activo = presenter.getAssetByName(appName);
                if (activo != null) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, AssetDetailView.newInstance(activo))
                            .setReorderingAllowed(true)
                            .addToBackStack("categoria")
                            .commit();

                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View rootView = requireView();
                    if (rootView != null) {
                        inputMethodManager.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    }

                    // Eliminar los intent anteriores
                    requireActivity().setIntent(new Intent());
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_catalogo_categoria, container, false);

        Bundle args = getArguments();
        if (args != null) {
            // Obtener el tipo del fragmento que lanzo este fragmento
            Tipo tipo = args.getParcelable(FRAGMENT_TIPO);
            presenter = new CatalogoTipoPresenter(tipo, this);
            presenter.init();

            // Link a los elemntos de la vista
            assetsTipoRV = layout.findViewById(R.id.assetsTipo_rv);
            LinearLayout sortLL = layout.findViewById(R.id.sort_ll);
            sortInfoTV = layout.findViewById(R.id.sortInfo_tv);

            // Configurar lista de dispositivos
            assetsTipoRV.setLayoutManager(new LinearLayoutManager(getContext()));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                    assetsTipoRV.getContext(),
                    DividerItemDecoration.VERTICAL);
            assetsTipoRV.addItemDecoration(dividerItemDecoration);

            // Ordenar dispositivos
            prefs = requireContext().getSharedPreferences("SortAssetsPrefs", Context.MODE_PRIVATE);
            String sortOrder = prefs.getString(PREF_ASSETS_CATALOGO_SORT_ORDER, getResources().getString(R.string.ordenarSeguridad_Asc));
            sortInfoTV.setText(sortOrder);
            updateRecyclerView(sortOrder);

            sortLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSortingOptionsPopup();
                }
            });

            // Establecer titulo en la barra de herramientas
            requireActivity().setTitle(presenter.getTipoName());
        }

        return layout;
    }

    private void updateRecyclerView(String sortOrder) {
        if (sortOrder.equals(getResources().getString(R.string.ordenarSeguridad_Desc))) {
            assetsTipoRV.setAdapter(new RVActivosPerfilAdapter(getContext(), presenter.getActivosTipoOrdenadosPorSeguridadDesc(), presenter.getActivosPerfil(), getMyApplication()));
        } else {
            assetsTipoRV.setAdapter(new RVActivosPerfilAdapter(getContext(), presenter.getActivosTipoOrdenadosPorSeguridadAsc(), presenter.getActivosPerfil(), getMyApplication()));
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
        prefs.edit().putString(PREF_ASSETS_CATALOGO_SORT_ORDER, sortOrder).apply();

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
        // No hay nada que actualizar
    }
}

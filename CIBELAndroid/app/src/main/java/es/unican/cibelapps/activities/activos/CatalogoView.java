package es.unican.cibelapps.activities.activos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import es.unican.cibelapps.R;
import es.unican.cibelapps.activities.activos.detail.AssetDetailView;
import es.unican.cibelapps.activities.activos.search.SearchResultView;
import es.unican.cibelapps.activities.main.MainView;
import es.unican.cibelapps.common.MyApplication;
import es.unican.cibelapps.model.Activo;

public class CatalogoView extends Fragment implements ICatalogoContract.View, MainView.RefreshableFragment {

    private ICatalogoContract.Presenter presenter;
    //private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CatalogoPresenter(this);
        presenter.init();
        setHasOptionsMenu(true);

        requestPermissionLauncher = registerForActivityResult(new RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permiso otorgado, acceder a las apps
                List<Activo> activosSincronizados = presenter.cargarAppsAutomatico();
                refreshData();
                showSyncedAppsMessage(activosSincronizados.size(), presenter.getNoDatosApps());
            } else {
                // Permiso denegado
                // Explicar al usuario que la función no está disponible porque
                // requiere un permiso que el usuario ha denegado.
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_catalogo, container, false);
        RecyclerView categoriasDevicesRV = layout.findViewById(R.id.categoriasDevices_rv);
        ImageView syncAppsIV = layout.findViewById(R.id.syncApps_iv);

        categoriasDevicesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriasDevicesRV.setAdapter(new RVTiposAdapter(getContext(), presenter.getTipos(), presenter.getPerfilAssets(), getMyApplication()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                categoriasDevicesRV.getContext(),
                DividerItemDecoration.VERTICAL);
        categoriasDevicesRV.addItemDecoration(dividerItemDecoration);

        syncAppsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.sync_apps_title);
                builder.setMessage(getResources().getString(R.string.info_sync_apps));
                builder.setPositiveButton(R.string.sync_apps_bt_possitive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Primero, verificar si el permiso ya está otorgado
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Permiso NO estaba otorgado, se solicita
                            dialogInterface.dismiss();
                            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        } else {
                            // Permiso ya estaba otorgado, acceder a las apps
                            dialogInterface.dismiss();
                            List<Activo> activosSincronizados = presenter.cargarAppsAutomatico();
                            refreshData();
                            showSyncedAppsMessage(activosSincronizados.size(), presenter.getNoDatosApps());
                        }
                    }
                });
                builder.setNegativeButton(R.string.sync_apps_bt_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return layout;
    }

    // Método para mostrar el mensaje informativo con el número de aplicaciones sincronizadas y las no disponibles
    private void showSyncedAppsMessage(int numAppsSincronizadas, List<String> unavailableApps) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¡Listo!");
        builder.setMessage("Se han añadido a tu perfil " + numAppsSincronizadas + " aplicaciones.\n\n"
                + "Lamentamos comunicarle que no disponemos de datos sobre las siguientes aplicaciones:\n"
                + formatUnavailableAppsList(unavailableApps));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método para formatear la lista de aplicaciones no disponibles
    private String formatUnavailableAppsList(List<String> unavailableApps) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String app : unavailableApps) {
            stringBuilder.append("- ").append(app).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtén el intent y verifica la acción
        Intent intent = requireActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showSearchResultView(query);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Maneja el clic en una sugerencia (porque todas las sugerencias usan ACTION_VIEW)
            Uri data = intent.getData();
            showAppDetailViewSuggestion(data);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showSearchResultView(String query) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SearchResultView.newInstance(query))
                .setReorderingAllowed(true)
                .addToBackStack("apps")
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
                            .addToBackStack("apps")
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

    @Override
    public MyApplication getMyApplication() {
        return (MyApplication) requireActivity().getApplication();
    }

    @Override
    public void showLoadError() {
        String text = getResources().getString(R.string.loadError);
        Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadCorrect(int appsCount) {
        String text = getResources().getString(R.string.loadCorrect);
        Toast.makeText(this.getContext(), String.format(text, appsCount), Toast.LENGTH_SHORT).show();
    }

    @Override
    public PackageManager getPackageManager() {
        return requireContext().getPackageManager();
    }

    @Override
    public void refreshData() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Eliminar los intent anteriores
        requireActivity().setIntent(new Intent());

        fragmentManager.beginTransaction()
                .replace(R.id.container, new CatalogoView())
                .commit();
    }

}

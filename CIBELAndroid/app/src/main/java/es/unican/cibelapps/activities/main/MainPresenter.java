package es.unican.cibelapps.activities.main;

import es.unican.cibelapps.R;
import es.unican.cibelapps.activities.activos.CatalogoView;
import es.unican.cibelapps.activities.smarthome.SmartHomeView;

public class MainPresenter implements IMainContract.Presenter {

    private final IMainContract.View view;

    public MainPresenter(IMainContract.View view) {
        this.view = view;
    }

    @Override
    public void onNavHomeClicked() {
        view.showFragment(new CatalogoView(), R.string.bottom_nav_home);
    }

    @Override
    public void onNavSmartHomeClicked() {
        view.showFragment(new SmartHomeView(), R.string.bottom_nav_perfil);
    }

}

package es.unican.cibel.activities.main;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

/**
 * Interfaz que define los métodos que deben ser implementados por el presentador y la vista
 * en la pantalla principal de la aplicación.
 */
public interface IMainContract {

    interface Presenter {
        /**
         * Debe ser usado por la vista cuando se pulse el icono de "Home" en el boton de
         * navegacion inferior.
         * Muestra el fragmento correspondiente a la pestaña "Home".
         */
        void onNavHomeClicked();

        void onNavSmartHomeClicked();
    }

    interface View {

        /**
         * Instancia y muestra en pantalla un fragmento poniendo el titulo indicado en la toolbar y
         * aplicando una sencilla animacion en el boton de navegacion inferior.
         * @param title Titulo que se desea que se muestre.
         */
        void showFragment(Fragment frg, @StringRes int title);
    }
}

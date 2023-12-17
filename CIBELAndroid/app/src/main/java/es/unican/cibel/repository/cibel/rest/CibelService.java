package es.unican.cibel.repository.cibel.rest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.unican.cibel.common.Callback;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.model.Vulnerabilidad;
import es.unican.cibel.repository.common.CallRunnable;
import es.unican.cibel.repository.common.CallbackAdapter;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para acceder a los recursos del servicio REST CIBELService usando Retrofit.
 */
public class CibelService {

    private CibelService() {
    }

    private static CibelAPI api;

    private static CibelAPI getAPI() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CibelServiceConstants.getAPIURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(CibelAPI.class);
        }
        return api;
    }

    public static final long TIMEOUT_SECONDS = 60L;

    /**
     * Descarga los activos de la API REST de forma asincrona.
     * Ejecuta la llamada en un hilo en segundo plano y notifica el resultado a
     * través del Callback proporcionado.
     * @param cb el callback que procesa la respuesta de forma asincrona
     * @param categoria se usa si se quiere que los activos se filtren por categoria,
     *                  sino dejarlo a NULL
     */
    public static void requestActivos(Callback<Activo[]> cb, String categoria) {
        final Call<Activo[]> call = getAPI().activos(categoria);
        call.enqueue(new CallbackAdapter<>(cb));
    }

    /**
     * Descarga los activos de la API REST de forma sincrona.
     * Bloquea el hilo actual hasta que se complete la llamada y se obtenga la respuesta.
     * @param categoria se usa si se quiere que los activos se filtren por categoria,
     *                  sino dejarlo a NULL
     * @return el objeto response que contiene los activos
     */
    public static Activo[] getActivos(String categoria) {
        final Call<Activo[]> call = getAPI().activos(categoria);

        ExecutorService executor = Executors.newFixedThreadPool(1);
        CallRunnable<Activo[]> runnable = new CallRunnable<>(call);
        executor.execute(runnable);

        // Espera a que acaben las tareas en background
        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Si hubo algun problema, response es null
        return  runnable.getResponse();
    }

    public static void requestTipos(Callback<Tipo[]> cb) {
        final Call<Tipo[]> call = getAPI().tipos();
        call.enqueue(new CallbackAdapter<>(cb));
    }

    public static Tipo[] getTipos() {
        final Call<Tipo[]> call = getAPI().tipos();

        ExecutorService executor = Executors.newFixedThreadPool(1);
        CallRunnable<Tipo[]> runnable = new CallRunnable<>(call);
        executor.execute(runnable);

        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return runnable.getResponse();
    }

    public static void requestVulnerabilidades(Callback<Vulnerabilidad[]> cb) {
        final Call<Vulnerabilidad[]> call = getAPI().vulnerabilidades();
        call.enqueue(new CallbackAdapter<>(cb));
    }

    public static Vulnerabilidad[] getVulnerabilidades() {
        final Call<Vulnerabilidad[]> call = getAPI().vulnerabilidades();

        ExecutorService executor = Executors.newFixedThreadPool(1);
        CallRunnable<Vulnerabilidad[]> runnable = new CallRunnable<>(call);
        executor.execute(runnable);

        executor.shutdown();
        try {
            executor.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return runnable.getResponse();
    }
}

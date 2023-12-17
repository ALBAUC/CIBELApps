package es.unican.cibel.repository.common;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Esta clase envuelve una peticion sincrona de una llamada de Retrofit en un Runnable.
 * Llamadas sincronas en Retrofit no pueden ser ejecutadas por un thread principal,
 * asique debe lanzarse un thread en background.
 * Este thread en background ejecutara este runnable.
 * @param <T> el tipo de la respuesta
 */
public class CallRunnable<T> implements Runnable {
    private final Call<T> call;
    private T response = null;

    public T getResponse(){
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public CallRunnable(Call<T> call) {
        this.call = call;
    }

    @Override
    public void run() {
        try {
            Response<T> answer = call.execute();
            this.setResponse(answer.body());
        } catch (IOException e) {
            Log.d("ERROR", "IOException lanzada en CallRunnable");
        }
    }
}
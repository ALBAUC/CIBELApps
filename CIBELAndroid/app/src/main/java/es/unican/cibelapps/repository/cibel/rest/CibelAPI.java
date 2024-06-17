package es.unican.cibelapps.repository.cibel.rest;

import es.unican.cibelapps.model.Activo;
import es.unican.cibelapps.model.Tipo;
import es.unican.cibelapps.model.Vulnerabilidad;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *  Acceso a CIBEL API usando Retrofit.
 */
public interface CibelAPI {

    @GET("apps")
    Call<Activo[]> activos(@Query("categoria") String categoria);

    @GET("apps/tipos")
    Call<Tipo[]> tipos();

    @GET("apps/vulnerabilidades")
    Call<Vulnerabilidad[]> vulnerabilidades();
}

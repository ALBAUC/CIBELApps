package es.unican.cibel.repository.cibel;

import es.unican.cibel.common.Callback;
import es.unican.cibel.model.Activo;
import es.unican.cibel.model.Tipo;
import es.unican.cibel.model.Vulnerabilidad;

/**
 * Un Repository para acceder a los recursos de CIBELService
 */
public interface ICibelRepository {

    /**
     * Solicita activos de forma asincrona.
     * Una vez que los activos han sido recuperadas de la fuente,
     * el callback indicado es llamado. Persiste los activos
     * en la base de datos local.
     * @param cb callback que procesa la respuesta de forma asíncrona
     * @param tipo se utiliza para filtrar los activos por categoría
     *                  (opcional, se puede dejar a null)
     */
    void requestActivos(Callback<Activo[]> cb, String tipo);

    /**
     * Solicita elementos digitales de forma sincrona.
     * Este metodo retorna una lista de elementos digitales directamente. Persiste los
     * elementos digitales en la base de datos local.
     * @param tipo
     * @return la lista de elementos digitales
     *          null si ocurre un error
     */
    Activo[] getActivos(String tipo);

    void requestTipos(Callback<Tipo[]> cb);

    Tipo[] getTipos();

    void requestVulnerabilidades(Callback<Vulnerabilidad[]> cb);

    Vulnerabilidad[] getVulnerabilidades();

    boolean lastDownloadOlderThan(int minutes, String resourceName);
}

package es.unican.cibel.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import es.unican.cibel.R;
import es.unican.cibel.repository.db.DaoSession;
import es.unican.cibel.repository.db.ActivoDao;
import es.unican.cibel.repository.db.TipoDao;
import es.unican.cibel.repository.db.VulnerabilidadDao;

@SuppressLint("ParcelCreator")
@Entity
public class Activo implements Parcelable {

    @SerializedName("id")
    @NonNull
    @Id
    private Long idActivo;

    private String nombre;

    private String icono;

    private Long fk_perfil;

    @ToOne(joinProperty = "fk_tipo")
    private Tipo tipo;
    private long fk_tipo;

    @ToMany
    @JoinEntity(
            entity = JoinActivosWithVulnerabilidades.class,
            sourceProperty = "activoId",
            targetProperty = "vulnerabilidadId"
    )
    private List<Vulnerabilidad> vulnerabilidades;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1317875841)
    private transient ActivoDao myDao;

    @Generated(hash = 879194533)
    public Activo(@NonNull Long idActivo, String nombre, String icono, Long fk_perfil, long fk_tipo) {
        this.idActivo = idActivo;
        this.nombre = nombre;
        this.icono = icono;
        this.fk_perfil = fk_perfil;
        this.fk_tipo = fk_tipo;
    }

    @Generated(hash = 315079783)
    public Activo() {
    }

    @Generated(hash = 606252662)
    private transient Long tipo__resolvedKey;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    @NonNull
    public Long getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(@NonNull Long idActivo) {
        this.idActivo = idActivo;
    }

    public Tipo getTipoTrampa() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Activo{" +
                "idActivo=" + idActivo +
                ", nombre='" + nombre + '\'' +
                ", icono='" + icono + '\'' +
                ", tipo=" + tipo +
                ", vulnerabilidades=" + vulnerabilidades +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(idActivo);
        parcel.writeString(nombre);
        parcel.writeString(icono);
    }

    public Long getFk_perfil() {
        return this.fk_perfil;
    }

    public void setFk_perfil(Long fk_perfil) {
        this.fk_perfil = fk_perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activo activo = (Activo) o;
        return idActivo.equals(activo.idActivo) && Objects.equals(nombre, activo.nombre) && Objects.equals(icono, activo.icono) && Objects.equals(tipo, activo.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActivo, nombre, icono, tipo);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public void setFk_perfil(long fk_perfil) {
        this.fk_perfil = fk_perfil;
    }

    public long getFk_tipo() {
        return this.fk_tipo;
    }

    public void setFk_tipo(long fk_tipo) {
        this.fk_tipo = fk_tipo;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1348072207)
    public Tipo getTipo() {
        long __key = this.fk_tipo;
        if (tipo__resolvedKey == null || !tipo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TipoDao targetDao = daoSession.getTipoDao();
            Tipo tipoNew = targetDao.load(__key);
            synchronized (this) {
                tipo = tipoNew;
                tipo__resolvedKey = __key;
            }
        }
        return tipo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1296182171)
    public void setTipo(@NonNull Tipo tipo) {
        if (tipo == null) {
            throw new DaoException("To-one property 'fk_tipo' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.tipo = tipo;
            fk_tipo = tipo.getIdTipo();
            tipo__resolvedKey = fk_tipo;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 842821075)
    public List<Vulnerabilidad> getVulnerabilidades() {
        if (vulnerabilidades == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VulnerabilidadDao targetDao = daoSession.getVulnerabilidadDao();
            List<Vulnerabilidad> vulnerabilidadesNew = targetDao._queryActivo_Vulnerabilidades(idActivo);
            synchronized (this) {
                if (vulnerabilidades == null) {
                    vulnerabilidades = vulnerabilidadesNew;
                }
            }
        }
        return vulnerabilidades;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1061741557)
    public synchronized void resetVulnerabilidades() {
        vulnerabilidades = null;
    }

    public int calcularPuntuacionSeguridad() {
        // Los mayores valores de totalGravedad rondan entre 350-400
        // Por lo que consideramos 400 como límite de mayor gravedad
        // Por eso divido entre 4
        int puntuacionSeguridad = (int) Math.round(100 - (calcularTotalGravedad() / 4));
        return Math.max(0, Math.min(100, puntuacionSeguridad));
    }

    public double calcularTotalGravedad() {
        List<Vulnerabilidad> vulnerabilidades = getVulnerabilidades();
        int totalGravedad = 0;

        for (Vulnerabilidad v : vulnerabilidades) {
            totalGravedad += v.getBaseScore() * 10; // baseScore de 0 a 100
        }

        return totalGravedad;
    }

    public int getColorFromTramo(int score) {
        int colorResId;
        if (score < 25) {
            colorResId = R.color.seekBar0;
        } else if (score < 50) {
            colorResId = R.color.seekBar1;
        } else if (score < 75) {
            colorResId = R.color.seekBar2;
        } else {
            colorResId = R.color.seekBar3;
        }
        return colorResId;
    }

    public int getEtiquetaSecurityFromTramo(int score) {
        int etiqueta;
        if (score < 25) {
            etiqueta = R.string.seguridad_0;
        } else if (score < 50) {
            etiqueta = R.string.seguridad_1;
        } else if (score < 75) {
            etiqueta = R.string.seguridad_2;
        } else {
            etiqueta = R.string.seguridad_3;
        }
        return etiqueta;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1926086416)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActivoDao() : null;
    }
}

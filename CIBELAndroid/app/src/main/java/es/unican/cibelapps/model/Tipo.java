package es.unican.cibelapps.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@SuppressLint("ParcelCreator")
@Entity
public class Tipo implements Parcelable {

    @SerializedName("id")
    @NonNull
    @Id
    private Long idTipo;

    private String nombre;
    private String nombre_en;

    @Generated(hash = 475983693)
    public Tipo(@NonNull Long idTipo, String nombre, String nombre_en) {
        this.idTipo = idTipo;
        this.nombre = nombre;
        this.nombre_en = nombre_en;
    }

    @Generated(hash = 1352377350)
    public Tipo() {
    }

    public Long getIdTipo() {
        return this.idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Tipo{" +
                "idTipo=" + idTipo +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nombre);
    }

    public String getNombre_en() {
        return this.nombre_en;
    }

    public void setNombre_en(String nombre_en) {
        this.nombre_en = nombre_en;
    }
}

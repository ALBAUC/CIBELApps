package es.unican.cibel.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinActivosWithVulnerabilidades {
    @Id
    private Long id;
    private Long activoId;
    private String vulnerabilidadId;
    @Generated(hash = 1565196382)
    public JoinActivosWithVulnerabilidades(Long id, Long activoId,
            String vulnerabilidadId) {
        this.id = id;
        this.activoId = activoId;
        this.vulnerabilidadId = vulnerabilidadId;
    }
    @Generated(hash = 885389194)
    public JoinActivosWithVulnerabilidades() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getActivoId() {
        return this.activoId;
    }
    public void setActivoId(Long activoId) {
        this.activoId = activoId;
    }
    public String getVulnerabilidadId() {
        return this.vulnerabilidadId;
    }
    public void setVulnerabilidadId(String vulnerabilidadId) {
        this.vulnerabilidadId = vulnerabilidadId;
    }
}

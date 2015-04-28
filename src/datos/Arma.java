/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import java.util.Date;

/**
 *
 * @author JAVA
 */
public class Arma {
    private Integer armaId;
    private String marca;
    private String calibre;
    private String serie;
    private Date fecha_registro;
    private String tipo;

    public Arma() {
    }

    public Arma(Integer arma_id, String marca, String calibre, String serie, Date fecha_registro, String tipo) {
        this.armaId = arma_id;
        this.marca = marca;
        this.calibre = calibre;
        this.serie = serie;
        this.fecha_registro = fecha_registro;
        this.tipo = tipo;
    }

    public Integer getArmaId() {
        return armaId;
    }

    public void setArmaId(Integer armaId) {
        this.armaId = armaId;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}

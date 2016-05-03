package com.edmundo.pfseguridad.Model;

/**
 * Created by edmundo on 5/2/16.
 */
public class Mensaje {

    private String mensajePlano;
    private String usuario;
    private String mensajeEncriptado;

    public Mensaje (String usuario, String mensajePlano, String mensajeEncriptado){

        this.usuario = usuario;
        this.mensajePlano = mensajePlano;
        this.mensajeEncriptado = mensajeEncriptado;

    }

    public String getMensajePlano() {
        return mensajePlano;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMensajeEncriptado() {
        return mensajeEncriptado;
    }
}

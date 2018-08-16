package com.epifi.tenlac;

public class Mensaje {
    private String mensaje;
    private String SendBy;

    public Mensaje(){

    }

    public Mensaje(String mensaje, String sendBy) {
        this.mensaje = mensaje;
        SendBy = sendBy;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSendBy() {
        return SendBy;
    }

    public void setSendBy(String sendBy) {
        SendBy = sendBy;
    }
}

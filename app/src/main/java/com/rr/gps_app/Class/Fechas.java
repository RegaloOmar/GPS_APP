package com.rr.gps_app.Class;

public class Fechas
{
    private String talon_Localidad;
    private String placas;
    private String sello;
    private String transportista;
    private String net;
    private String fecha_Cita;
    private String confirmacion;
    private String fecha_Cita_Hora;

    public Fechas(String talon_Localidad, String placas, String sello, String transportista, String net, String fecha_Cita, String confirmacion, String fecha_Cita_Hora) {
        this.talon_Localidad = talon_Localidad;
        this.placas = placas;
        this.sello = sello;
        this.transportista = transportista;
        this.net = net;
        this.fecha_Cita = fecha_Cita;
        this.confirmacion = confirmacion;
        this.fecha_Cita_Hora = fecha_Cita_Hora;

    }

    public void setTalon_Localidad(String talon_Localidad) {
        this.talon_Localidad = talon_Localidad;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setFecha_Cita(String fecha_Cita) {
        this.fecha_Cita = fecha_Cita;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }

    public void setFecha_Cita_Hora(String fecha_Cita_Hora) {
        this.fecha_Cita_Hora = fecha_Cita_Hora;
    }

    public String getTalon_Localidad() {
        return talon_Localidad;
    }

    public String getPlacas() {
        return placas;
    }


    public String getSello() {
        return sello;
    }

    public String getTransportista() {
        return transportista;
    }

    public String getNet() {
        return net;
    }

    public String getFecha_Cita() {
        return fecha_Cita;
    }


    public String getConfirmacion() {
        return confirmacion;
    }

    public String getFecha_Cita_Hora() {
        return fecha_Cita_Hora;
    }

}

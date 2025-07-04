package tpFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class zonaDeCobertura implements Observable {
	private String nombre;
    private Ubicacion epicentro;
    private double radioKm;
    private List<Muestra> muestras;
    private List<Observador> observadores;
    
    //Constructor
    public zonaDeCobertura(String nombre, Ubicacion epicentro, double radioKm, List<Muestra> muestras) {
        this.nombre = nombre;
        this.epicentro = epicentro;
        this.radioKm = radioKm;
        this.muestras = muestras;
        this.observadores = new ArrayList<>(); 
    }
    
    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public Ubicacion getEpicentro() {
        return epicentro;
    }

    public double getRadioKm() {
        return radioKm;
    }
    
    public List<Muestra> getMuestrasDentro() {
        return muestras.stream()
            .filter(m -> epicentro.distanciaA(m.getUbicacion()) <= radioKm)
            .toList();
    }
    
    
    //Metodos de zonaDeCobertura
    @Override
    public void suscribir(Observador observador) {
        observadores.add(observador);
    }

    @Override
    public void desuscribir(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarSubida(Muestra muestra) {
        for (Observador o : observadores) {
            o.notificarSubida(muestra, this);
        }
    }

    @Override
    public void notificarValidacion(Muestra muestra) {
        for (Observador o : observadores) {
            o.notificarValidacion(muestra, this);
        }
    }

    
    //Se invoca desde el adminDeZonas y notifica validacion a sus observadores
    public void muestraValidada(Muestra muestra) {
        if (this.muestras.contains(muestra)) {
            this.notificarValidacion(muestra);
        }
    }
    //Notifica la nueva muestra a sus observadores
    public void agregarMuestra(Muestra m) {
        this.muestras.add(m);
        this.notificarSubida(m);
    }

    public boolean contieneMuestra(Muestra m) {
        return epicentro.distanciaA(m.getUbicacion()) <= radioKm;
    }

    public boolean seSolapaCon(zonaDeCobertura otra) {
        double distanciaEntreCentros = this.epicentro.distanciaA(otra.epicentro);
        return distanciaEntreCentros <= (this.radioKm + otra.radioKm);
    }
}

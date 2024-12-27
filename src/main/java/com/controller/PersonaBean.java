package com.controller;

import com.dao.PersonaDao;
import com.model.Persona;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "personaBean")
@RequestScoped
public class PersonaBean {

    @Inject // Inyecta el DAO automáticamente
    private PersonaDao personaDao;

    // Obtener todas las personas
    public List<Persona> obtenerPersonas() {
        return personaDao.obtenerPersonas();
    }

    // Editar persona
    public String editar(int id) {
        Persona persona = personaDao.buscar(id);
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("persona", persona);
        return "editar?faces-redirect=true";
    }

    // Actualizar persona
    public String actualizar(Persona persona) {
        personaDao.editar(persona);
        return "index?faces-redirect=true";
    }

    public String eliminar(int id) {
        System.out.println("Eliminando persona con id: " + id);
        personaDao.delete(id);
        System.out.println("Persona eliminada.");
        return "index?faces-redirect=true"; // Redirige después de la eliminación
    }

    public String nuevo(){
        Persona persona = new Persona();
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("persona", persona);
        return "prueba?faces-redirect=true";
    }

    public String guardar(Persona persona) {
        System.out.println("*************************************************");
        System.out.println("Persona: " + persona);
        System.out.println("*************************************************");
        personaDao.guardar(persona);
        return "index?faces-redirect=true";
    }

    public long getTotalPersonas() {
        return personaDao.contarTotalPersonas();
    }

    public long getPersonasImportantes() {
        return personaDao.contarPorImportancia("alto");
    }

    public long getPersonasMedias() {
        return personaDao.contarPorImportancia("medio");
    }

    public long getPersonasBajas() {
        return personaDao.contarPorImportancia("bajo");
    }
    private String nombreBusqueda; // Criterio de búsqueda
    private List<Persona> personasFiltradas; // Resultados de la búsqueda

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    public List<Persona> getPersonasFiltradas() {
        if (personasFiltradas == null || nombreBusqueda == null || nombreBusqueda.isEmpty()) {
            return personaDao.obtenerPersonas();
        }
        return personasFiltradas;
    }

    public void buscar() {
        personasFiltradas = personaDao.buscarPorNombre(nombreBusqueda);
    }


}

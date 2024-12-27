package com.dao;

import com.model.Persona;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless // Marca esta clase como un EJB para que el contenedor gestione su ciclo de vida
public class PersonaDao {

    @PersistenceContext(unitName = "jsf-crud-unit")
        private EntityManager entityManager; // El contenedor inyectará automáticamente el EntityManager

    // Guardar cliente
    public void guardar(Persona persona) {
        if (persona.getId() == 0) {
            System.out.println("Guardando una nueva persona con ID generado...");
        } else {
            System.out.println("Actualizando persona con ID: " + persona.getId());
        }

        if (entityManager != null) {
            entityManager.persist(persona);
        } else {
            System.out.println("EntityManager is null!");
        }
    }

    // Editar cliente
    public void editar(Persona persona) {
        entityManager.merge(persona);
    }

    // Buscar cliente por ID
    public Persona buscar(int id) {
        return entityManager.find(Persona.class, id);
    }

    // Obtener todos los clientes
    public List<Persona> obtenerPersonas() {
        Query query = entityManager.createQuery("SELECT p FROM Persona p");
        return query.getResultList();
    }

    public void delete(int id){
        Persona persona = new Persona();
        System.out.println("-------------------------------------------------------");
        System.out.println("Id en PersonaDao: "+id);
        System.out.println("-------------------------------------------------------");
        persona = entityManager.find(Persona.class, id);
        System.out.println("Persona: "+persona);
        entityManager.remove(persona); // Si existe, elimina la entidad
    }

    public List<Persona> cantPersonasImportantes() {
        Query query = entityManager.createQuery("SELECT p FROM Persona p");
        return query.getResultList();
    }
    public long contarTotalPersonas() {
        Query query = entityManager.createQuery("SELECT COUNT(p) FROM Persona p");
        return (long) query.getSingleResult();
    }

    // Cantidad de personas por importancia
    public long contarPorImportancia(String importancia) {
        Query query = entityManager.createQuery("SELECT COUNT(p) FROM Persona p WHERE p.importancia = :importancia");
        query.setParameter("importancia", importancia);
        return (long) query.getSingleResult();
    }
    public List<Persona> buscarPorNombre(String nombre) {
        Query query = entityManager.createQuery("SELECT p FROM Persona p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)");
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }
}

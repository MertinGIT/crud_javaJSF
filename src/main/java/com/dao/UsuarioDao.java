package com.dao;

import com.model.Usuario;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UsuarioDao {

    @PersistenceContext(unitName = "jsf-crud-unit")
    private EntityManager entityManager;

    public void guardar(Usuario usuario) {
        entityManager.persist(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        try {
            Query query = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
            query.setParameter("username", username);
            return (Usuario) query.getSingleResult();
        } catch (Exception e) {
            return null; // Usuario no encontrado
        }
    }

    public Usuario buscarPorEmail(String email) {
        try {
            return (Usuario) entityManager.createQuery("SELECT u FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Si no se encuentra, devuelve null
        }
    }

}

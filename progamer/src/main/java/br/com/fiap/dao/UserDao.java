package br.com.fiap.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.fiap.model.User;

public class UserDao {

	EntityManagerFactory factory = 
			Persistence.createEntityManagerFactory("progamer-persistence-unit");
	EntityManager manager = factory.createEntityManager();
	//private EntityManager manager;
	
	public void create(User user) {
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		
		manager.clear();
		
	}
	
	public List<User> listAll(User user) {
		String jpql = "SELECT u FROM User u WHERE email=:email";
		TypedQuery<User> query = manager.createQuery(jpql , User.class);
		query.setParameter("email", user.getEmail());
		return query.getResultList();
	}

	public User exist(User user) {
		String jpql = "SELECT u FROM User u WHERE email=:email AND password=:password";
		TypedQuery<User> query = manager.createQuery(jpql , User.class);
		query.setParameter("email", user.getEmail());
		query.setParameter("password", user.getPassword());
		
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}

}

package com.nitya.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.nitya.hibernate.model.User;

public class UserDAO {

	Transaction transaction = null;
	Session session = null;
	SessionFactory sessionFactory = null;

	public UserDAO(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	public boolean saveUser(User user) {

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			System.out.println("user details: " + user.getUsername());

			session.save(user);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}

			e.printStackTrace();

			return false;
		}
	}

	public boolean login(User user) {

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			System.out.println("user details: " + user.getEmail()+", "+user.getPassword());

			Query<User> query = session.createQuery("from User where email = :email and password = :password",
					User.class);
			query.setParameter("email", user.getEmail());
			query.setParameter("password", user.getPassword());

			User userResult = query.uniqueResult();

			transaction.commit();

			if (userResult != null) {
				System.out.println("Login successful for user: " + user.getEmail());
				System.out.println("Retrieved user: " + userResult.getEmail() + ", " + userResult.getPassword());
				return true;
			} else {
				System.out.println("Login failed for user: " + user.getEmail());
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}

			e.printStackTrace();

			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
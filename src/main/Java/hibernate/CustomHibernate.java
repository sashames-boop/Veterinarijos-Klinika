package hibernate;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.*;

import java.util.Collections;
import java.util.List;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }


    public User getUserByCredentials(String login, String password) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            //Man sitos klases reik, kad susilipdyciau salygas or(), and() ... metodu forma
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(
                    cb.and(
                            cb.equal(root.get("username"), login),
                            cb.equal(root.get("password"), password))
            );

            TypedQuery<User> q = entityManager.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            //Sito reikia ir likusiuose metoduose
            if (entityManager != null) entityManager.close();
        }
    }

    public static User getUserByUsername(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(
                    cb.equal(root.get("username"), username)
            );
            TypedQuery<User> q = entityManager.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public static LoginStatus validateLogin(String username, String password) {
        User currentUser = getUserByUsername(username);
        if(currentUser == null) {
            return LoginStatus.USER_NOT_FOUND;
        }
        if(password == null || !password.equals(currentUser.getPassword())) {
            return LoginStatus.WRONG_PASSWORD;
        }
        if(currentUser.isBlocked()) {
            return LoginStatus.BLOCKED;
        }
        return LoginStatus.SUCCESS;
    }

    public static void assignPet(Integer clientId, Integer petId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Client client = entityManager.find(Client.class, clientId);
            Pet pet = entityManager.find(Pet.class, petId);

            if(client == null || pet == null) {
                throw new IllegalArgumentException("Client or pet does not exist.");
            }
            if(pet.getOwner() != null && !pet.getOwner().equals(client)) {
                throw new IllegalArgumentException("This pet already has an owner");
            }
            client.registerPet(pet);

            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }


    public List<User> getUsersBytType(List<Class<? extends User>> types) {
        if(types.isEmpty()) {
            return getAll(User.class, "");
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where( root.type().in(types) );
            return entityManager.createQuery(query).getResultList();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void createRegistration(Integer petId, Integer specialistId, String date, String time) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            Pet pet = em.find(Pet.class, petId);
            User specialist = em.find(User.class, specialistId);

            if (pet == null || pet.getOwner() == null) {
                throw new IllegalArgumentException("Choose a pet with an owner.");
            }
            if (pet.getOwner().isBlocked()) {
                throw new IllegalArgumentException("The owner is blocked.");
            }
            if (specialist == null || specialist.isBlocked()) {
                throw new IllegalArgumentException("Choose an active specialist.");
            }

            Registration registration;
            if (specialist instanceof Vet vet) {
                registration = new Registration(date, time, pet.getOwner(), pet, vet);
            } else if (specialist instanceof Groomer groomer) {
                registration = new Registration(date, time, pet.getOwner(), pet, groomer);
            } else {
                throw new IllegalArgumentException("Choose a vet or groomer.");
            }

            em.persist(registration);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

}

package hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import hibernate.CustomHibernate;

import java.util.List;


public class GenericHibernate {
    protected static EntityManagerFactory entityManagerFactory = null;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    // READ
    public <T> List<T> getAll(Class<T> entityClass, String queryString) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> criteria = cb.createQuery(entityClass);
            Root<T> root = criteria.from(entityClass);

            if(queryString != null && !queryString.equals("")) {
                for (String q : queryString.split(" ")) {
                    root.fetch(q, JoinType.LEFT);
                }
            }

            criteria.select(root).distinct(true);
            TypedQuery<T> query = entityManager.createQuery(criteria);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    // CREATE
    public <T> void create(T entity) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public <T> void update(T entity) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE
    public <T> void delete(T entity) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            T managed = em.contains(entity) ? entity : em.merge(entity);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // FIND
    public <T> T getEntityById(Class<T> entityClass, Object id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
}

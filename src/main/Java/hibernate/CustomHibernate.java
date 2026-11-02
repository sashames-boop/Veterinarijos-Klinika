package hibernate;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.User;

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
                            cb.like(root.get("username"), login),
                            cb.like(root.get("password"), password))
            );

            Query q = entityManager.createQuery(query);
            return (User) q.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //Sito reikia ir likusiuose metoduose
            if (entityManager != null) entityManager.close();
        }
    }
}

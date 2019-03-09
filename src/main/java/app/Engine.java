package app;

import app.entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Scanner;

public class Engine implements Runnable {
    //ako properti e final se povada zaduljitelno prez ctor
    private final EntityManager entityManager;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void run() {
        this.containceEmployee();
    }

    /*
    Contains employees
     */
    private void containceEmployee() {
        Scanner read = new Scanner(System.in);

        String name = read.nextLine();
        this.entityManager.getTransaction().begin();

        try {
            Employee employee = this.entityManager
                    .createQuery("FROM Employee WHERE concat(first_name ,' ' ,last_name) = :name ", Employee.class)
                    .setParameter("name", name)
                    .getSingleResult();

            System.out.println("Yes");
        } catch (NoResultException ex) {
            System.out.println("No");
        }

        this.entityManager.getTransaction().commit();
    }
}

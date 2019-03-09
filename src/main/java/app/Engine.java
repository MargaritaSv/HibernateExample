package app;

import app.entities.Address;
import app.entities.Employee;
import app.entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Scanner;

public class Engine implements Runnable {
    private final EntityManager entityManager;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void run() {
        //  this.containsEmployee();

        this.addAndUpdate();
    }

    /*
    Contains employees
     */
    private void containsEmployee() {
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

    /*
    Adding a new address and update
     */
    private void addAndUpdate() {
        Scanner read = new Scanner(System.in);

        String name = read.nextLine();

        this.entityManager.getTransaction().begin();

        Address address = new Address();
        address.setText("Vitoshka 15");

        Town town = this.entityManager.createQuery("FROM Town WHERE name = 'Sofia'", Town.class)
                .getSingleResult();

        address.setTown(town);
        this.entityManager.persist(address);

        Employee employee = this.entityManager.createQuery("FROM Employee WHERE last_name = :name", Employee.class)
                .setParameter("name", name)
                .getSingleResult();

        //first detach and than set new address
        this.entityManager.detach(employee.getAddress());

        employee.setAddress(address);

        this.entityManager.merge(employee);

        this.entityManager.getTransaction().commit();
    }
}

package com.greatlearning.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int selected = -1;
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "echoes1#");
            UsersDAO usersDAO = new UsersDAO();
            do {
                showMenu();
                selected = in.nextInt();
                in.nextLine();
                switch (selected) {
                    case 1:
                        doRegistration(in, usersDAO, connection);
                        break;
                    case 2:
                        update(in, usersDAO, connection);
                        break;
                    case 3:
                        show(in, usersDAO, connection);
                        break;
                    case 4:
                        delete(in, usersDAO, connection);
                        break;
                }
            } while (selected != 0);
        } catch (SQLException e) {
            System.out.println("Error establishing connection");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        in.close();
    }

    private static void delete(Scanner in, UsersDAO usersDAO, Connection connection) {
        System.out.println("Enter user id to delete");
        Integer id = in.nextInt();
        in.nextLine();
        try {
            int rows = usersDAO.delete(connection, id);
            if (rows > 0) {
                System.out.println("Delete successful for existing user id: " + id);
            } else if (rows == 0) {
                System.out.println("Row does not exist with user id: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Delete failed");
        }
    }

    private static void show(Scanner in, UsersDAO usersDAO, Connection connection) {
        try {
            usersDAO.getData(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to display data");
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("!!! Welcome to User CRUD Services");
        System.out.println();
        System.out.println("=============================================");
        System.out.println("Enter the operation that you want to peform");
        System.out.println("1. Registration");
        System.out.println("2. Update");
        System.out.println("3. Display data");
        System.out.println("4. Delete");
        System.out.println("0. Exit");
        System.out.println("=============================================");
    }

    private static void doRegistration(Scanner in, UsersDAO usersDAO, Connection connection) {
        System.out.println("Registration started");
        Integer id;
        String firstName;
        String lastName;
        String email;
        System.out.println("Enter user id:");
        id = in.nextInt();
        in.nextLine();
        System.out.println("Enter first name:");
        firstName = in.nextLine();
        System.out.println("Enter last name:");
        lastName = in.nextLine();
        System.out.println("Enter email address:");
        email = in.nextLine();
        User user = new User(id, firstName, lastName, email);

        try {
            usersDAO.saveData(connection, user);
            System.out.println("Registration successful for user id: " + id);
        } catch (SQLException e) {
            if (isConstraintViolation(e)) {
                System.out.println("User already registerd with user id =" + id + " use another id for registration");
            }
            System.out.println("Registration failed");
        }

    }

    private static void update(Scanner in, UsersDAO usersDAO, Connection connection) {
        Integer id;
        String firstName;
        String lastName;
        String email;
        System.out.println("Enter user id to update");
        id = in.nextInt();
        in.nextLine();
        UserBuilder userBuilder = new UserBuilder(id);
        int selected = -1;
        do {
            System.out.println("Select column to update");
            System.out.println("0. confirm update");
            System.out.println("1. first name");
            System.out.println("2. last name");
            System.out.println("3. email");
            selected = in.nextInt();
            in.nextLine();
            switch (selected) {
                case 1:
                    System.out.println("Enter new first name");
                    firstName = in.nextLine();
                    userBuilder.setFirstName(firstName);
                    break;
                case 2:
                    System.out.println("Enter new last name");
                    lastName = in.nextLine();
                    userBuilder.setLastName(lastName);
                    break;
                case 3:
                    System.out.println("Enter new email");
                    email = in.nextLine();
                    userBuilder.setEmail(email);
                    break;
            }
        } while (selected != 0);

        User user = userBuilder.createUser();
        try {
            int rows = usersDAO.updateData(connection, user);
            if (rows > 0) {
                System.out.println("Update successful for existing user id: " + id);
            } else if (rows == 0) {
                System.out.println("Row does not exist with user id: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Update failed");
        }

    }

    private static boolean isConstraintViolation(SQLException e) {
        return e.getSQLState().startsWith("23");
    }
}

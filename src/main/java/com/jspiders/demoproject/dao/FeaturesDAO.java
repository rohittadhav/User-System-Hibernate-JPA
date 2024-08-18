package com.jspiders.demoproject.dao;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.jspiders.demoproject.dto.UserDTO;

public class FeaturesDAO {

	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;
	private static EntityTransaction entityTransaction;
	private Scanner scanner = new Scanner(System.in);

	// Add user
	public void addUser() {

		openConnection();

		System.out.println("Enter user name: ");
		String name = scanner.nextLine();

		System.out.println("Enter user email: ");
		String email = scanner.nextLine();

		System.out.println("Enter user mobile: ");
		long mobile = scanner.nextLong();
		scanner.nextLine();

		System.out.println("Enter user password: ");
		String password = scanner.nextLine();

		String role = null;
		while (true) {
			System.out.println("Enter 1 for User");
			System.out.println("Enter 2 for Admin");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				role = "user";
				break;
			case 2:
				if (adminExist()) {
					System.out.println("Admin is already exists. You are added role as user.");
					role = "user";
				} else {
					role = "admin";
				}
				break;
			default:
				System.out.println("Invalid choice.");
				break;
			}
			break;
		}

		UserDTO user = new UserDTO();
		user.setName(name);
		user.setEmail(email);
		user.setMobile(mobile);
		user.setPassword(password);
		user.setRole(role);

		entityTransaction.begin();
		entityManager.persist(user);
		entityTransaction.commit();

		System.out.println("User added successfully.");

		closeConnection();
	}

	// To check admin is already exists or not
	private boolean adminExist() {
		try {
			TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM UserDTO u WHERE u.role = 'admin'",
					Long.class);
			Long count = query.getSingleResult();
			return count > 0;
		} catch (Exception e) {
			return false;
		}
	}

	// Fetch user by Id
	public void fetchUserById(int userId, int fetchId) {
		openConnection();
		UserDTO user = entityManager.find(UserDTO.class, fetchId);

		if (user == null) {
			System.out.println("User not found.");
		} else {
			UserDTO loggedInUser = entityManager.find(UserDTO.class, userId);
			String userRole = loggedInUser.getRole();
			
			if ("admin".equalsIgnoreCase(userRole)) {
				displayUserDetails(user);
			} else if ("user".equalsIgnoreCase(userRole) && user.getId() == userId) {
				displayUserDetails(user);
			} else {
				System.out.println("Access Denied: You can only view your own details.");
			}
		}
		closeConnection();
	}

	// Display user details
	private void displayUserDetails(UserDTO user) {
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("User found here are the details: ");
		System.out.println("Id: " + user.getId());
		System.out.println("Name: " + user.getName());
		System.out.println("Email: " + user.getEmail());
		System.out.println("Mobile: " + user.getMobile());
		System.out.println("Password: " + user.getPassword());
		System.out.println("Role: " + user.getRole());
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	// Fetch user by email and password
	public void fetchUserByEmailAndPassword(String userEmail, String userPassword) {
		openConnection();
		UserDTO user = null;

		try {
			String jquery = "SELECT u FROM UserDTO u WHERE u.email = :email AND u.password = :password";
			TypedQuery<UserDTO> query = entityManager.createQuery(jquery, UserDTO.class);
			query.setParameter("email", userEmail);
			query.setParameter("password", userPassword);

			user = query.getSingleResult();
			if (user != null) {
				String userRole = user.getRole();
				if ("admin".equalsIgnoreCase(userRole)) {
					displayUserDetails(user);
				} else if ("user".equalsIgnoreCase(userRole) && userEmail.equals(user.getEmail())
						&& userPassword.equals(user.getPassword())) {
					displayUserDetails(user);
				}
			} else {
				System.out.println("Access Denied: You can only view your own details.");
			}

		} catch (Exception e) {
			System.out.println("Invalid email or password. No user found.");
		} finally {
			closeConnection();
		}
	}

	// Display all users
	public void displayAllUsers(int userId) {
		openConnection();

		UserDTO user = entityManager.find(UserDTO.class, userId);

		if (user == null) {
			System.out.println("Invalid user id. User not found.");
		} else {
			String userRole = user.getRole();

			if ("admin".equalsIgnoreCase(userRole)) {

				String jquery = "SELECT u FROM UserDTO u";
				TypedQuery<UserDTO> query = entityManager.createQuery(jquery, UserDTO.class);
				List<UserDTO> usersList = query.getResultList();

				if (usersList.isEmpty()) {
					System.out.println("No users found.");
				} else {
					for (UserDTO user1 : usersList) {
						displayUserDetails(user1);
					}
				}
			} else if ("user".equalsIgnoreCase(userRole)) {
				displayUserDetails(user);
			} else {
				System.out.println("Access Denied: Invalid role.");
			}
		}
		closeConnection();
	}

	// Update user
	public void updateUser(int id, int updateId) {
		openConnection();
		UserDTO user = entityManager.find(UserDTO.class, updateId);

		if (user == null) {
			System.out.println("User not found.");
		} else {
			UserDTO loggedInUser = entityManager.find(UserDTO.class, id); // Finding log-in user by id
			String userRole = loggedInUser.getRole();

			if ("admin".equalsIgnoreCase(userRole) || ("user".equalsIgnoreCase(userRole) && id == updateId)) {

				boolean flag = true;
				while (flag) {
					System.out.println("Choose the field you want to update: ");
					System.out.println("1. Name");
					System.out.println("2. Email");
					System.out.println("3. Password");
					System.out.println("4. Mobile");
					System.out.println("5. Exit");

					int choice = scanner.nextInt();
					scanner.nextLine();

					switch (choice) {
					case 1:
						System.out.println("Enter new name: ");
						String newName = scanner.nextLine();
						user.setName(newName);
						System.out.println("Name updated successfully.");
						break;
					case 2:
						System.out.println("Enter new email: ");
						String newEmail = scanner.nextLine();
						System.out.println("Email updated successfully.");
						user.setEmail(newEmail);
						break;
					case 3:
						System.out.println("Enter new password: ");
						String newPassword = scanner.nextLine();
						user.setPassword(newPassword);
						System.out.println("Password updated successfully.");
						break;
					case 4:
						System.out.println("Enter new mobile: ");
						long newMobile = scanner.nextLong();
						user.setMobile(newMobile);
						System.out.println("Mobile updated successfully.");
						break;
					case 5:
						flag = false;
						System.out.println("Exiting from the update..");
						break;
					default:
						System.out.println("Invalid choice. Please select a valid option.");
						continue;
					}

					entityTransaction.begin();
					entityManager.persist(user);
					entityTransaction.commit();

					System.out.println("Details Updated Successfully.");
				}
			} else {
				System.out.println("Access Denied: You can only update your own details.");
			}
		}

		closeConnection();
	}

	// Delete user
	public void deleteUser(int id, int deleteId) {
		openConnection();

		UserDTO user = entityManager.find(UserDTO.class, deleteId);

		if (user == null) {
			System.out.println("User not found.");
		} else {
			UserDTO loggedInUser = entityManager.find(UserDTO.class, id);
			String userRole = loggedInUser.getRole();

			if ("admin".equalsIgnoreCase(userRole) || ("user".equalsIgnoreCase(userRole) && id == deleteId)) {
				entityTransaction.begin();
				entityManager.remove(user);
				entityTransaction.commit();

				System.out.println("User deleted successfully.");
			} else {
				System.out.println("Access Denied: You can only delete your own account.");
			}
		}
		closeConnection();
	}

	private static void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory("demoproject");
		entityManager = entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
	}

	private static void closeConnection() {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
		if (entityManager != null) {
			entityManager.close();
		}
		if (entityTransaction != null) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		}
	}

}

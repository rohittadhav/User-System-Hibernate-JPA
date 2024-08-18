package com.jspiders.demoproject.dao;

import java.util.Scanner;

public class OperationDAO {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		FeaturesDAO features = new FeaturesDAO();

		System.out.println("*************************************************************************************************************************************************************************************************");
		System.out.println("!!! WELCOME !!!");
		System.out.println("*************************************************************************************************************************************************************************************************");

		boolean flag = true;

		while (flag) {
			System.out.println("<==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==>");
			System.out.println("Enter 1 for Add user");
			System.out.println("Enter 2 for Fetch user by ID");
			System.out.println("Enter 3 for Fetch user by Email and Password");
			System.out.println("Enter 4 for Display all users");
			System.out.println("Enter 5 for Update user");
			System.out.println("Enter 6 for Delete user");
			System.out.println("Enter 7 for Exit");
			System.out.println("<==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==><==>");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				features.addUser();
				break;

			case 2:
				System.out.println("Enter your id: ");
				int userId = scanner.nextInt();
				scanner.nextLine();

				System.out.println("Enter user id you want to fetch: ");
				int fetchId = scanner.nextInt();

				features.fetchUserById(userId, fetchId);
				break;

			case 3:
				System.out.println("Enter user email: ");
				String userEmail = scanner.nextLine();

				System.out.println("Enter user password: ");
				String userPassword = scanner.nextLine();

				features.fetchUserByEmailAndPassword(userEmail, userPassword);
				break;

			case 4:
				System.out.println("Enter your id: ");
				int userid = scanner.nextInt();

				features.displayAllUsers(userid);
				break;

			case 5:
				System.out.println("Enter your id: ");
				int id = scanner.nextInt();

				System.out.println("Enter user id you want to update: ");
				int updateId = scanner.nextInt();

				features.updateUser(id, updateId);
				break;

			case 6:
				System.out.println("Enter your id: ");
				int uId = scanner.nextInt();

				System.out.println("Enter user id you want to delete");
				int deleteId = scanner.nextInt();

				features.deleteUser(uId, deleteId);
				break;

			case 7:
				flag = false;
				System.out.println("Exiting..Thank You!!");
				break;

			default:
				System.out.println("Invalid choice. Please try again");
				break;
			}
		}
		scanner.close();
	}

}

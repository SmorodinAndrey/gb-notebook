package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;
import notebook.util.UserValidator;

import java.util.Scanner;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        Commands com;

        while (true) {
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT)
                return;
            switch (com) {
                case CREATE:
                    String firstName = prompt("Введите Имя: ");
                    String lastName = prompt("Введите Фамилию: ");
                    String phone = prompt("Введите телефон: ");
                    userController.createUser(firstName, lastName, phone);
                    break;
                case READ:
                    String id = prompt("Индетификатор пользвателя: ");
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case LIST:
                    System.out.println(userController.readAll());
                    break;
                case DELETE:
                    String deleteId = prompt("Индетификатор пользвателя: ");
                    userController.deleteUser(deleteId);
                    break;
                case UPDATE:
                    String userId = prompt("Индетификатор пользвателя: ");
                    String updateFirstName = prompt("Введите новое Имя: ");
                    String updateLastName = prompt("Введите новую Фамиию: ");
                    String updatePhone = prompt("Введите новый телефон: ");

                    User updatedUser = new User(updateFirstName, updateLastName, updatePhone);
                    userController.updateUser(userId, updatedUser);
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
}

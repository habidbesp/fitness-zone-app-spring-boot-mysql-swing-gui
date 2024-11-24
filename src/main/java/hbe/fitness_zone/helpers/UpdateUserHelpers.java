package hbe.fitness_zone.helpers;

import hbe.fitness_zone.model.Client;
import hbe.fitness_zone.service.IClientService;

import java.util.Scanner;

public class UpdateUserHelpers {
    public static int showUpdateUserMenu(Scanner console, Client clientToUpdate){
        System.out.println("Client to update: " + clientToUpdate);
        System.out.print("""
                Update Client Menu:
                1. Update firstname
                2. Update lastname
                3. Update membership number
                4. Back to menu
                Choose an option:\s""");
        return Integer.parseInt(console.nextLine());
    }

    public static boolean executeOptionsMenu(
            int option,
            Scanner console,
            IClientService clientService,
            Client clientById
    ){
        var exit = false;

        switch (option){
            case 1 -> {
                updateFirstname(console, clientService, clientById);
                exit = true;
            }
            case 2 -> {
                updateLastname(console, clientService, clientById);
                exit = true;
            }
            case 3 -> {
                updateMembership(console, clientService, clientById);
                exit = true;
            }
            case 4 -> exit = true;
            default -> System.out.println("Invalid Option: " + option);
        }
        return exit;
    }

    public static void updateFirstname(Scanner console,
                                       IClientService clientService, Client clientById){
        try{
            System.out.print("Provide a new firstname: ");
            var newFirstname = console.nextLine();
            if (newFirstname.trim().isEmpty())
                throw new Exception("Provide a valid new firstname");

            var clientToUpdate = new Client(clientById.getId(), newFirstname,
                    clientById.getLastname(), clientById.getMembership());

            clientService.saveClient(clientToUpdate);

            System.out.println("Firstname: " + clientToUpdate.getFirstname() + ", successfully updated!");
            System.out.println(clientToUpdate);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public static void updateLastname(Scanner console,
                                      IClientService clientService, Client clientById){
        try{
            System.out.print("Provide a new lastname: ");
            var newLastname = console.nextLine();
            if (newLastname.trim().isEmpty())
                throw new Exception("Provide a valid new lastname");

            var clientToUpdate = new Client(clientById.getId(), clientById.getFirstname(),
                    newLastname, clientById.getMembership());

            clientService.saveClient(clientToUpdate);

            System.out.println("Lastname: " + clientToUpdate.getLastname() + ", successfully updated!");
            System.out.println(clientToUpdate);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void updateMembership(Scanner console,
                                        IClientService clientService, Client clientById){
        try {
            System.out.print("Provide a new membership number: ");
            String membershipInput = console.nextLine();

            if (membershipInput.trim().isEmpty())
                throw new Exception("Membership number cannot be empty.");
            int membership;
            try {
                membership = Integer.parseInt(membershipInput);
            } catch (NumberFormatException e) {
                throw new Exception("Membership number must be a valid number.");
            }

            var clientToUpdate = new Client(clientById.getId(), clientById.getFirstname(),
                    clientById.getLastname(), membership);
            clientService.saveClient(clientToUpdate);

            System.out.println("Membership number: " + clientToUpdate.getMembership() + ", successfully updated!");
            System.out.println(clientToUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

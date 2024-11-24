package hbe.fitness_zone;

import hbe.fitness_zone.helpers.UpdateUserHelpers;
import hbe.fitness_zone.model.Client;
import hbe.fitness_zone.service.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

// Disabling spring boot factory
// @SpringBootApplication
public class FitnessZoneApplication implements CommandLineRunner {

	@Autowired
	private IClientService clientService;

	private static final Logger logger =
			LoggerFactory.getLogger(FitnessZoneApplication.class);

	String nl = System.lineSeparator();

	public static void main(String[] args) {
		logger.info("Initializing app");
		// Initializing Spring factory
		SpringApplication.run(FitnessZoneApplication.class, args);
		logger.info("Application finalized!🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️🏃🏻‍♂️‍➡️");
	}

	@Override
	public void run(String... args) throws Exception {
		fitnessZoneApp();
	}

	private void fitnessZoneApp(){
		try {
			var exit = false;
			var console = new Scanner(System.in);
			while (!exit){
				try {
					var option = showMenu(console);
					exit = executeOptions(option, console);
					logger.info(nl);
				} catch (Exception e) {
					System.out.println("An error has occurred: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private int showMenu(Scanner console) {
		logger.info("""
                \n*** Fitness Zone Application (GYM) ***
                1. List all clients
                2. Find a client by id
                3. Register a client
                4. Update client info
                5. Delete a client
                6. Logout (close app)
                Choose an option:\s""");
		return Integer.parseInt(console.nextLine());
	}

	private boolean executeOptions(int option, Scanner console){

		var exit = false;
		switch (option){
			case 1 -> listClients();
			case 2 -> findClient(console);
			case 3 -> registerClient(console);
			case 4 -> updateClientInfo(console);
			case 5 -> deleteClient(console);
			case 6 -> {
				logger.info("Thanks for visiting Fitness Zone, come back anytime!" + nl + nl);
				exit = true;
			}
			default -> System.out.println("Invalid Option: " + option);
		}
		return exit;
	}

	private void listClients(){
		var clients = clientService.listAllClients();
		logger.info(nl + "--- Clients List ---" + nl);
		clients.forEach(client -> logger.info(client.toString() + nl));
	}

	private void findClient(Scanner console){
		try{
			logger.info(nl + "--- Find Client by Id ---" + nl);
			logger.info("Provide client id: ");
			var clientIdInput = console.nextLine();
			if (clientIdInput.trim().isEmpty())
				throw new Exception("Client id number cannot be empty.");
			int clientId;
			try {
				clientId = Integer.parseInt(clientIdInput);
			} catch (NumberFormatException e) {
				throw new Exception("Client id number must be a valid number.");
			}

			var clientById = clientService.findClientById(clientId);

			if(clientById != null)
				logger.info("Client found" + clientById + nl);
			else
				logger.info("Client with id " + clientId + " was not found!" + nl);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void registerClient(Scanner console){
		try{
			logger.info("--- Add new Client ---" + nl);
			logger.info("Provide a firstname: ");
			var firstname = console.nextLine();
			if (firstname.trim().isEmpty())
				throw new Exception("Provide a valid firstname");

			logger.info("Provide a lastname: ");
			var lastname = console.nextLine();
			if (lastname.trim().isEmpty())
				throw new Exception("Provide a valid lastname");

			logger.info("Provide a membership number: ");
			String membershipInput = console.nextLine();
			if (membershipInput.trim().isEmpty())
				throw new Exception("Membership number cannot be empty.");
			int membership;
			try {
				membership = Integer.parseInt(membershipInput);
			} catch (NumberFormatException e) {
				throw new Exception("Membership number must be a valid number.");
			}

			var newClient = new Client();
			newClient.setFirstname(firstname);
			newClient.setLastname(lastname);
			newClient.setMembership(membership);

			clientService.saveClient(newClient);

			if (newClient.getId() != null){
				logger.info(newClient.getFirstname() + " was registered successfully!" + nl);
				logger.info("" + newClient);
			}
			else
				logger.info(newClient.getFirstname() + " was not registered.");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void updateClientInfo(Scanner console){
		try{
			logger.info("--- Update client info ---" + nl);
			logger.info("Provide client's id: ");
			String idInput = console.nextLine();

			if (idInput.trim().isEmpty())
				throw new Exception("Id number cannot be empty.");
			int clientId;
			try {
				clientId = Integer.parseInt(idInput);
			} catch (NumberFormatException e) {
				throw new Exception("Id number must be a valid number.");
			}

			var clientToUpdate = clientService.findClientById(clientId);

			if (clientToUpdate != null ){
				var exit = false;
				while (!exit){
					try{
						var option = UpdateUserHelpers.showUpdateUserMenu(console, clientToUpdate);
						exit = UpdateUserHelpers.executeOptionsMenu(option, console, clientService, clientToUpdate);
					}
					catch (Exception e) {
						logger.info("An error has occurred: " + e.getMessage() + nl);
					}
				}
			}else
				logger.info("Client with id " + clientId + " not found."  + nl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void deleteClient(Scanner console){
		try{
			logger.info("To delete a client, provide client id: ");
			String idInput = console.nextLine();
			if (idInput.trim().isEmpty())
				throw new Exception("Id number cannot be empty.");
			int idClient;
			try {
				idClient = Integer.parseInt(idInput);
			} catch (NumberFormatException e) {
				throw new Exception("Id must be a valid number.");
			}

			var clientToDelete = clientService.findClientById(idClient);


			if(clientToDelete != null){
				clientService.deleteClient(clientToDelete);
				System.out.println("Client was successfully deleted");
				System.out.println(clientToDelete);
			}
			else
				System.out.println("Client to delete not found");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

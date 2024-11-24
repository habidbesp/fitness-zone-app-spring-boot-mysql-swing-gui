package hbe.fitness_zone.service;

import hbe.fitness_zone.model.Client;
import hbe.fitness_zone.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements IClientService{

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public List<Client> listAllClients() {
        List<Client> clients = clientRepo.findAll();
        return clients;
    }

    @Override
    public Client findClientById(Integer idClient) {
        Client client = clientRepo.findById(idClient).orElse(null);
        return client;
    }

    @Override
    public void saveClient(Client client) {
        clientRepo.save(client);

    }

    @Override
    public void deleteClient(Client client) {
        clientRepo.delete(client);
    }
}

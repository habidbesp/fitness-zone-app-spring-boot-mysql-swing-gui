package hbe.fitness_zone.service;

import hbe.fitness_zone.model.Client;

import java.util.List;

public interface IClientService {

    public List<Client> listAllClients();
    public Client findClientById(Integer idClient);
    public void saveClient(Client client);
    public void deleteClient(Client client);



}

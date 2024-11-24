package hbe.fitness_zone.repository;


import hbe.fitness_zone.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Integer> {

}

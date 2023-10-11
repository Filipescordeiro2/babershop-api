package com.ProjetoPI.BaberShop.Repository;

import com.ProjetoPI.BaberShop.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository <Cliente,Long> {

    boolean existsByEmail(String email);

    Optional<Cliente> findByEmail(String email);

}

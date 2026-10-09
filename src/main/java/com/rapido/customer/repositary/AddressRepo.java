package com.rapido.customer.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rapido.customer.entity.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer> {

}

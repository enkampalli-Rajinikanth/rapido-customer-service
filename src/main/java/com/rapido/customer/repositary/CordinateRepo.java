package com.rapido.customer.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rapido.customer.entity.Cordinate;
@Repository
public interface CordinateRepo  extends JpaRepository<Cordinate, Integer>{

}

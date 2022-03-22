package com.mikehenry.springbootslice.repository;

import com.mikehenry.springbootslice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, AddressPK> {
}

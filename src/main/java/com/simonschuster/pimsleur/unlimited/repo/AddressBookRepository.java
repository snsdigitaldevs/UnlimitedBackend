package com.simonschuster.pimsleur.unlimited.repo;

import com.simonschuster.pimsleur.unlimited.data.domain.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Integer> {
}

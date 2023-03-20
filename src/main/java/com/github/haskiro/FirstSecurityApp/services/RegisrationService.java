package com.github.haskiro.FirstSecurityApp.services;

import com.github.haskiro.FirstSecurityApp.models.Person;
import com.github.haskiro.FirstSecurityApp.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisrationService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public RegisrationService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void register(Person person) {
        peopleRepository.save(person);
    }


}

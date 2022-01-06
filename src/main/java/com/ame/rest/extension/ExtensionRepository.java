package com.ame.rest.extension;

import java.util.List;

import com.ame.rest.user.developer.Developer;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ExtensionRepository extends Repository<Extension, Long> {
    Extension findById(Long id);
    List<Extension> findByDeveloper(Developer developer);
    Extension save(Extension extension);
    List<Extension> findAll();
}

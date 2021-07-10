package com.mer.rest.extension;

import org.springframework.data.repository.Repository;

public interface ExtensionRepository extends Repository<Extension, Long> {
    Extension findById(Long id);
    Extension save(Extension extension);
    Iterable<Extension> findAll();
}

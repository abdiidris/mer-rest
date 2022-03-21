package com.ame.rest.extension.instance;

import org.springframework.data.repository.Repository;

public interface CopyRepo extends Repository<Copy, Long> {
    Copy findById(Long id);
    void save(Copy copy);
}

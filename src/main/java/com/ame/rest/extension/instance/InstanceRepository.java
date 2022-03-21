package com.ame.rest.extension.instance;
import com.ame.rest.user.writer.Writer;

import org.springframework.data.repository.Repository;

public interface InstanceRepository extends Repository<Instance, Long> {
    Instance findById(Long id);
    Iterable<Instance> findByWriter(Writer writer);
    Instance save(Instance Instance);
    Iterable<Instance> findAll();
}

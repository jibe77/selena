package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.RadioStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by jibe on 18/08/16.
 */
public interface RadioStationRepository extends CrudRepository<RadioStation, Long> {

    RadioStation findByChannel(@Param("channel") int channel);

}

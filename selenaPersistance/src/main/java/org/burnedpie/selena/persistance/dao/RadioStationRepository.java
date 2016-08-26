package org.burnedpie.selena.persistance.dao;

import org.burnedpie.selena.persistance.domain.RadioStation;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jibe on 18/08/16.
 */
public interface RadioStationRepository extends CrudRepository<RadioStation, Long> {

    RadioStation findByChannel(int channel);

}

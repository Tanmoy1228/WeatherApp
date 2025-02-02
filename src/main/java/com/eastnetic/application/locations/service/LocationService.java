package com.eastnetic.application.locations.service;

import com.eastnetic.application.locations.entity.Location;
import com.eastnetic.application.locations.entity.LocationDetails;

public interface LocationService {

    Location getLocationById(long id);

    Location addLocation(LocationDetails locationDetails);

    void deleteLocation(Location location);

    Location getLocationByReferenceIdAndSource(String id, String source);
}

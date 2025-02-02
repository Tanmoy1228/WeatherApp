package com.eastnetic.application.views;

import com.eastnetic.application.locations.entity.LocationDetails;
import com.eastnetic.application.locations.exceptions.LocationDataException;
import com.eastnetic.application.locations.service.LocationProviderService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AnonymousAllowed
@UIScope
@Component
@Route(value = "locations", layout = MainLayout.class)
@PageTitle("Locations")
public class LocationSearchView extends VerticalLayout {

    private static final Logger LOGGER = LogManager.getLogger(LocationSearchView.class);

    private TextField searchField = new TextField();

    private Button searchButton = new Button("Search");

    private final LocationListComponent locationListComponent;

    private final LocationProviderService locationProviderService;

    public LocationSearchView(LocationListComponent locationListComponent,
                              LocationProviderService locationProviderService) {

        this.locationListComponent = locationListComponent;
        this.locationProviderService = locationProviderService;

        setSizeFull();

        add(getSearchForm(), locationListComponent);

        searchButton.addClickListener(e -> searchLocations());
    }

    private FormLayout getSearchForm() {

        searchField.setPlaceholder("Search by City Name");

        searchButton.addClickShortcut(Key.ENTER);

        FormLayout searchForm = new FormLayout(searchField, searchButton);
        searchForm.setWidth("50%");

        return searchForm;

    }

    private void searchLocations() {

        String cityName = searchField.getValue();

        if (cityName != null && !cityName.isEmpty()) {

            try {

                List<LocationDetails> locations = locationProviderService.getLocationDetails(cityName, 50);

                locationListComponent.showLocationList(locations);

            } catch (LocationDataException ex) {

                locationListComponent.setVisible(false);

                Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE);

            } catch (Exception ex) {

                LOGGER.error("Error fetching location data: {}", ex.getMessage(), ex);

                Notification.show("Error fetching location data. Please try again later.", 3000, Notification.Position.MIDDLE);
            }
        }
    }

}

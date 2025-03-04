package org.fzengin.app.route.application;

import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.data.dto.TransportationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class RouteAppValidRouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteAppTransportationDataService transportationDataService;

    @Autowired
    private RouteAppLocationDataService locationDataService;

    @BeforeEach
    public void setUpInMemoryDb() {
        saveLocationsToInMemoryDb();
        saveTransportationsToInMemoryDb();
    }

    @Test
    public void testForValidDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/route/get")
                        .param("originLocationName", "KARSIYAKA")
                        .param("destinationLocationName", "TAKSIM")
                        .param("date", "2025-03-03")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Valid routes were found."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].transportationDtoList").isArray())
                .andExpect(jsonPath("$.data[0].transportationDtoList[0].originLocation").value("KARSIYAKA"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[0].destinationLocation").value("ADNAN MENDERES HAVAALANI"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[0].transportationType").value("BUS"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[1].originLocation").value("ADNAN MENDERES HAVAALANI"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[1].destinationLocation").value("SABIHA GOKCEN HAVAALANI"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[1].transportationType").value("FLIGHT"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[2].originLocation").value("SABIHA GOKCEN HAVAALANI"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[2].destinationLocation").value("TAKSIM"))
                .andExpect(jsonPath("$.data[0].transportationDtoList[2].transportationType").value("BUS"))
                .andExpect(jsonPath("$.data.length()").value(1));
    }


    @Test
    public void testForNotValidDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/route/get")
                        .param("originLocationName", "KARSIYAKA")
                        .param("destinationLocationName", "TAKSIM")
                        .param("date", "2025-03-05")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Can not found valid route"));


    }

    private void saveTransportationsToInMemoryDb() {
        TransportationDto flightFromSawToAdb = new TransportationDto();
        flightFromSawToAdb.setTransportationType("FLIGHT");
        flightFromSawToAdb.setDestinationLocation("ADNAN MENDERES HAVAALANI");
        flightFromSawToAdb.setOriginLocation("SABIHA GOKCEN HAVAALANI");
        flightFromSawToAdb.setOperationDays(Set.of(1, 2, 4, 5, 7));

        TransportationDto flightFromAdbToSaw = new TransportationDto();
        flightFromAdbToSaw.setTransportationType("FLIGHT");
        flightFromAdbToSaw.setDestinationLocation("SABIHA GOKCEN HAVAALANI");
        flightFromAdbToSaw.setOriginLocation("ADNAN MENDERES HAVAALANI");
        flightFromAdbToSaw.setOperationDays(Set.of(1, 4, 7));

        TransportationDto busFromTaksimToKarsiyaka = new TransportationDto();
        busFromTaksimToKarsiyaka.setTransportationType("BUS");
        busFromTaksimToKarsiyaka.setDestinationLocation("TAKSIM");
        busFromTaksimToKarsiyaka.setOriginLocation("KARSIYAKA");
        busFromTaksimToKarsiyaka.setOperationDays(Set.of(1, 2, 4, 5, 7));

        TransportationDto busFromSawToTaksim = new TransportationDto();
        busFromSawToTaksim.setTransportationType("BUS");
        busFromSawToTaksim.setDestinationLocation("TAKSIM");
        busFromSawToTaksim.setOriginLocation("SABIHA GOKCEN HAVAALANI");
        busFromSawToTaksim.setOperationDays(Set.of(1, 2, 4, 5, 7));

        TransportationDto busFromKarsiyakaToAdb = new TransportationDto();
        busFromKarsiyakaToAdb.setTransportationType("BUS");
        busFromKarsiyakaToAdb.setDestinationLocation("ADNAN MENDERES HAVAALANI");
        busFromKarsiyakaToAdb.setOriginLocation("KARSIYAKA");
        busFromKarsiyakaToAdb.setOperationDays(Set.of(1, 2, 3, 4, 5, 6, 7));

        TransportationDto busFromKarsiyakaToTaksim = new TransportationDto();
        busFromKarsiyakaToTaksim.setTransportationType("BUS");
        busFromKarsiyakaToTaksim.setDestinationLocation("KARSIYAKA");
        busFromKarsiyakaToTaksim.setOriginLocation("TAKSIM");
        busFromKarsiyakaToTaksim.setOperationDays(Set.of(1, 2, 3, 4, 5, 6, 7));

        transportationDataService.saveTransportation(flightFromAdbToSaw);
        transportationDataService.saveTransportation(flightFromSawToAdb);
        transportationDataService.saveTransportation(busFromKarsiyakaToTaksim);
        transportationDataService.saveTransportation(busFromSawToTaksim);
        transportationDataService.saveTransportation(busFromKarsiyakaToAdb);
        transportationDataService.saveTransportation(busFromTaksimToKarsiyaka);

    }

    private void saveLocationsToInMemoryDb() {
        LocationDto sabihaGokcen = new LocationDto();
        sabihaGokcen.setName("SABIHA GOKCEN HAVAALANI");
        sabihaGokcen.setCountry("Turkey");
        sabihaGokcen.setLocationCode("SAW");

        LocationDto adnanMenderes = new LocationDto();
        adnanMenderes.setName("ADNAN MENDERES HAVAALANI");
        adnanMenderes.setCountry("Turkey");
        adnanMenderes.setLocationCode("ADM");

        LocationDto karsiyaka = new LocationDto();
        karsiyaka.setName("KARSIYAKA");
        karsiyaka.setCountry("Turkey");
        karsiyaka.setLocationCode("KSK");

        LocationDto taksim = new LocationDto();
        taksim.setName("TAKSIM");
        taksim.setCountry("Turkey");
        taksim.setLocationCode("TKSMM");

        locationDataService.saveLocation(sabihaGokcen);
        locationDataService.saveLocation(adnanMenderes);
        locationDataService.saveLocation(karsiyaka);
        locationDataService.saveLocation(taksim);
    }
}

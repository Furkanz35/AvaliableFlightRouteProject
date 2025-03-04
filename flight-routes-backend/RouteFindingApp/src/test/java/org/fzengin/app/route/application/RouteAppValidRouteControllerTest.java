package org.fzengin.app.route.application;

import org.fzengin.app.route.data.dto.LocationDto;
import org.fzengin.app.route.data.dto.TransportationDto;
import org.fzengin.app.route.data.dto.enums.TransportationType;
import org.fzengin.app.route.service.RouteAppLocationDataService;
import org.fzengin.app.route.service.RouteAppTransportationDataService;
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

    public static final String ADB = "ADNAN MENDERES";

    public static final String SAW = "SABIHA GOKCEN";

    public static final String TAKSIM = "TAKSIM";

    public static final String KSK = "KARSIYAKA";

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
                .andExpect(jsonPath("$.data[0].transportationDtoList[0].originLocationName").value(KSK))
                .andExpect(jsonPath("$.data[0].transportationDtoList[0].destinationLocationName").value(ADB))
                .andExpect(jsonPath("$.data[0].transportationDtoList[0].transportationType").value(TransportationType.SUBWAY.name()))
                .andExpect(jsonPath("$.data[0].transportationDtoList[1].originLocationName").value(ADB))
                .andExpect(jsonPath("$.data[0].transportationDtoList[1].destinationLocationName").value(SAW))
                .andExpect(jsonPath("$.data[0].transportationDtoList[1].transportationType").value(TransportationType.FLIGHT.name()))
                .andExpect(jsonPath("$.data[0].transportationDtoList[2].originLocationName").value(SAW))
                .andExpect(jsonPath("$.data[0].transportationDtoList[2].destinationLocationName").value(TAKSIM))
                .andExpect(jsonPath("$.data[0].transportationDtoList[2].transportationType").value(TransportationType.UBER.name()))
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
        LocationDto adnanMenderes = locationDataService.findLocationByLocationName(ADB).orElse(null);
        LocationDto sabihaGokcen = locationDataService.findLocationByLocationName(SAW).orElse(null);
        LocationDto karsiyaka = locationDataService.findLocationByLocationName(KSK).orElse(null);
        LocationDto taksim = locationDataService.findLocationByLocationName(TAKSIM).orElse(null);

        assert adnanMenderes != null;
        assert sabihaGokcen != null;
        assert karsiyaka != null;
        assert taksim != null;


        TransportationDto flightFromSawToAdb = new TransportationDto(sabihaGokcen.getId(), adnanMenderes.getId(), TransportationType.FLIGHT.name(),
                Set.of(1, 2, 4, 5, 7));
        TransportationDto flightFromAdbToSaw = new TransportationDto(adnanMenderes.getId(), sabihaGokcen.getId(), TransportationType.FLIGHT.name(),
                Set.of(1, 4, 7));
        TransportationDto busFromTaksimToKarsiyaka = new TransportationDto(taksim.getId(), karsiyaka.getId(), TransportationType.BUS.name(),
                Set.of(1, 2, 3, 4, 5, 6, 7));
        TransportationDto uberFromSawToTaksim = new TransportationDto(sabihaGokcen.getId(), taksim.getId(), TransportationType.UBER.name(),
                Set.of(1, 2, 4, 5, 7));
        TransportationDto subwayFromKarsiyakaToAdb = new TransportationDto(karsiyaka.getId(), adnanMenderes.getId(),
                TransportationType.SUBWAY.name(), Set.of(1, 2, 3, 4, 5, 6, 7));
        TransportationDto busFromKarsiyakaToTaksim = new TransportationDto(karsiyaka.getId(), taksim.getId(), TransportationType.BUS.name(),
                Set.of(1, 2, 3, 4, 5, 6, 7));

        transportationDataService.saveTransportation(flightFromAdbToSaw);
        transportationDataService.saveTransportation(flightFromSawToAdb);
        transportationDataService.saveTransportation(busFromKarsiyakaToTaksim);
        transportationDataService.saveTransportation(uberFromSawToTaksim);
        transportationDataService.saveTransportation(subwayFromKarsiyakaToAdb);
        transportationDataService.saveTransportation(busFromTaksimToKarsiyaka);

    }

    private void saveLocationsToInMemoryDb() {
        String country = "TURKEY";
        locationDataService.saveLocation(new LocationDto(SAW, country, "SAW"));
        locationDataService.saveLocation(new LocationDto(ADB, country, "ADM"));
        locationDataService.saveLocation(new LocationDto(KSK, country, "KSK"));
        locationDataService.saveLocation(new LocationDto(TAKSIM, country, "TKSM"));
    }
}

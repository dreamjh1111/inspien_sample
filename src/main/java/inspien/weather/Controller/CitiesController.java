package inspien.weather.Controller;

import inspien.weather.Entity.CitiesList;
import inspien.weather.Repository.CitiesListRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * URL: https://inspientest.duckdns.org/citylist
 * Method: GET
 * 날씨 조회가 가능한 도시 리스트를 조회
 */
@RestController
@RequestMapping("/citylist")
public class CitiesController {
    private final CitiesListRepository citiesListRepository;

    public CitiesController(CitiesListRepository citiesListRepository) {
        this.citiesListRepository = citiesListRepository;
    }

    @GetMapping
    public List<CitiesList> getCityListData() {
        return citiesListRepository.findAll();
    }
}



package inspien.weather.WeatherAPITest;

import inspien.weather.Service.WeatherService;
import inspien.weather.WeatherApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WeatherApplication.class)
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @BeforeEach
    public void setup() {
        // weatherService의 saveCurrentWeatherData 메서드가 호출되어도 아무 동작도 수행하지 않도록 설정합니다.
        doNothing().when(weatherService).getCurrentWeatherData(anyString());
    }

    @Test
    public void testGetCurrentWeather() throws Exception {
        String city = "Seoul";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/{city}", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Weather data saved for city: " + city))
                .andDo(print());

        // assert를 사용하여 응답의 상태 코드와 내용을 비교합니다.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/{city}", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Weather data saved for city: " + city))
                .andDo(print())
                .andReturn()
                .getResponse();

        // 또는 다음과 같이 assert를 사용하여 결과를 비교할 수 있습니다.
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/{city}", city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Weather data saved for city: " + city))
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals("Weather data saved for city: " + city, responseBody);
    }
}

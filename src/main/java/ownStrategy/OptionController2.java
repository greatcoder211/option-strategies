package ownStrategy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ownStrategy.DTO.CompanyDTO;
import ownStrategy.network.TickerSearch;

import java.util.List;

@RestController
public class OptionController2 {
    private final OptionService2 optionService;
    private final TickerSearch tickerSearch;
    public OptionController2(OptionService2 optionService, TickerSearch tickerSearch) {
        this.optionService = optionService;
        this.tickerSearch = tickerSearch;
    }
    @GetMapping("/companies")
    public List<CompanyDTO> getCompanies(@RequestParam String key) {
        return tickerSearch.Companies(key);
    }
}

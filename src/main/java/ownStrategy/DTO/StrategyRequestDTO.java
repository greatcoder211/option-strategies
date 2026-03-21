package ownStrategy.DTO;

import java.util.List;

public record StrategyRequestDTO (
    String name,
    List<OptionLegDTO> legs,
    double entryPrice,
    double timeToExpiry,
    int quantity
){}

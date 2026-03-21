package ownStrategy.DTO;

import ownStrategy.sPattern.Belfort;
import ownStrategy.sPattern.OptionType;

public record OptionLegDTO (
    double strikePrice,
    OptionType type,
    Belfort belfort
) {}

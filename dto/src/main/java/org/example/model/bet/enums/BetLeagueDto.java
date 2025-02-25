package org.example.model.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@Slf4j
public enum BetLeagueDto {
    ITALY("2019"),
    PREMIER_LEAGUE("2021"),
    SPAIN("2014");

    private final String id;

    public static BetLeagueDto fromString(String betLeague) {

        try {
            return BetLeagueDto.valueOf(betLeague);
        } catch (Exception e) {
            log.info(String.format("%s is not valid betLeague value", betLeague));
            log.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
}

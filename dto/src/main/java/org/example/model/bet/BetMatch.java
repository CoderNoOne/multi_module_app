package org.example.model.bet;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.bet.enums.MatchStatus;

import java.time.ZonedDateTime;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BetMatch {

    @JsonProperty(value ="id")
    private Integer id;

    private MatchScore score;
    private MatchStatus status;
    private ZonedDateTime lastUpdated;

    private ZonedDateTime utcDate;
    private Integer currentMatchday;
    private Integer matchday;

    private Long min;

    private BetTeam homeTeam;
    private BetTeam awayTeam;

    @JsonSetter(value = "season")
    public void setCurrentMatchday(Map<String, Object> seasonProperties) {
        this.currentMatchday = (Integer) seasonProperties.get("currentMatchday");
    }
}

package org.example.model.fm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.fm.enums.Formation;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchSquadDto {

    private Integer id;

    private Integer matchId;
    private TeamDto teamDto;

    private Formation formation;
    private Integer substitutionsNumberAvailable;

    private Map<String, PlayerDto> players;

    public Map<String, PlayerDto> substitutions;


}

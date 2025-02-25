package org.example.model.fm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamSquadDto {

    private Integer id;

    private Integer matchId;
    private SquadDto squadDto;
    private TeamDto teamDto;
}

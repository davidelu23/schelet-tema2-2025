package models.milestones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Repartition {
    private String developer;
    private List<Integer> assignedTickets;

    public Repartition(String developer) {
        this.developer = developer;
        assignedTickets = new ArrayList<>();
    }
}

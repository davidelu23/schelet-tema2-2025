package models.milestones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the repartition of tickets for a developer in a milestone.
 */
@Getter
@Setter
@NoArgsConstructor
public class Repartition {
    private String developer;
    private List<Integer> assignedTickets;

    /**
     * Constructs a new Repartition.
     * @param developer The developer's username.
     */
    public Repartition(final String developer) {
        this.developer = developer;
        assignedTickets = new ArrayList<>();
    }
}

package xyz.zlatanov.subsbuddy.query.availablesubs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class AvailableSubsQuery {

	private String owner;
}

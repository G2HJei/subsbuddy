package xyz.zlatanov.subsbuddy.web.query.availablesubs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Data
@Accessors(fluent = true)
public class AvailableSubsProjection {

	private List<SubDetails> result;

}

package xyz.zlatanov.subsbuddy.query.availablesubs;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class AvailableSubsProjection {

	private List<SubDetails> result = new ArrayList<>();
}

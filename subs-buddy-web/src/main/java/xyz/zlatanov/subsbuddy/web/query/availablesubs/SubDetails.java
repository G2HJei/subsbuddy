package xyz.zlatanov.subsbuddy.web.query.availablesubs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.core.domain.Language;

@Data
@Accessors(fluent = true)
public class SubDetails {

	private UUID						id;
	private String						filename;
	private Language					language;
	private List<TranslationDetails>	translations	= new ArrayList<>();
}

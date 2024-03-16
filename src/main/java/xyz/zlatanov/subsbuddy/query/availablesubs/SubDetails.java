package xyz.zlatanov.subsbuddy.query.availablesubs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.domain.Language;

@Data
@Accessors(fluent = true)
public class SubDetails {

	private String						id;
	private String						filename;
	private Language					language;
	private List<TranslationDetails>	translations	= new ArrayList<>();
}

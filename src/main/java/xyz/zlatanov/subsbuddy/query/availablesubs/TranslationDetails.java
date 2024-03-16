package xyz.zlatanov.subsbuddy.query.availablesubs;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.domain.Language;
import xyz.zlatanov.subsbuddy.domain.Translation;

@Data
@Accessors(fluent = true)
public class TranslationDetails {

	private String				id;
	private Language			language	= BG;
	private Translation.Status	status;
}

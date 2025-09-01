package xyz.zlatanov.subsbuddy.web.query.availablesubs;

import static xyz.zlatanov.subsbuddy.core.domain.Language.BG;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.core.domain.Language;
import xyz.zlatanov.subsbuddy.web.domain.TranslationStatus;

@Data
@Accessors(fluent = true)
public class TranslationDetails {

	private UUID				id;
	private Language			language	= BG;
	private TranslationStatus	status;
}

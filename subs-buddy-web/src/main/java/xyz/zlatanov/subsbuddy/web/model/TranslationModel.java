package xyz.zlatanov.subsbuddy.web.model;

import static xyz.zlatanov.subsbuddy.core.domain.Language.BG;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TranslationModel {

	private String	id;
	private String	language	= BG.name();
	private String	status;
}

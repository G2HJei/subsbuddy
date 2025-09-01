package xyz.zlatanov.subsbuddy.web.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SubtitleModel {

	private String					id;
	private String					filename;
	private String					language;
	private List<TranslationModel>	translations	= new ArrayList<>();
}

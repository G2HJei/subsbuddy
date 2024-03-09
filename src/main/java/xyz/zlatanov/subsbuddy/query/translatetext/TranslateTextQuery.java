package xyz.zlatanov.subsbuddy.query.translatetext;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TranslateTextQuery {

	private String text;
}

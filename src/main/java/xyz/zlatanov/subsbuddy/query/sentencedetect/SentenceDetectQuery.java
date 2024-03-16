package xyz.zlatanov.subsbuddy.query.sentencedetect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode
public class SentenceDetectQuery {

	private String text;
}

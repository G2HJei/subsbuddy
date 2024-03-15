package xyz.zlatanov.subsbuddy.query.sentencedetection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode
public class SentenceDetectionQuery {

	private String text;
}

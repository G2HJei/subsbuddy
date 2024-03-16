package xyz.zlatanov.subsbuddy.query.sentencedetect;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SentenceDetectProjection {

	private List<String> sentenceList = new ArrayList<>();
}

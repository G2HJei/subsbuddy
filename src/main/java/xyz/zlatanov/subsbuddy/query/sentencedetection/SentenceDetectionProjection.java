package xyz.zlatanov.subsbuddy.query.sentencedetection;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SentenceDetectionProjection {

	private List<String> sentenceList = new ArrayList<>();
}

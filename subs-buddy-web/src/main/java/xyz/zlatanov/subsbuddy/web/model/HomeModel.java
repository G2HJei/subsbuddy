package xyz.zlatanov.subsbuddy.web.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class HomeModel {

	private Long				quota;
	private List<SubtitleModel>	subtitleList;
}

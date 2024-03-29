package xyz.zlatanov.subsbuddy.query.download;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DownloadFileProjection {

	private String	filename;
	private String	content;
}

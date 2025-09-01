package xyz.zlatanov.subsbuddy.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DownloadModel {

	private String	filename;
	private byte[]	content;
}

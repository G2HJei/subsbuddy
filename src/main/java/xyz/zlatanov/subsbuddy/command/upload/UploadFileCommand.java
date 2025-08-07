package xyz.zlatanov.subsbuddy.command.upload;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class UploadFileCommand {

	private String	filename;
	private String	content;
}

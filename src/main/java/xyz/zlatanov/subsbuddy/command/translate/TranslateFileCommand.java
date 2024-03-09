package xyz.zlatanov.subsbuddy.command.translate;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TranslateFileCommand {

	private String	id;
}

package xyz.zlatanov.subsbuddy.command.translate;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TranslateFileCommand {

	private UUID id;
}

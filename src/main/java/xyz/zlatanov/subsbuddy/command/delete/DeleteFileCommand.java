package xyz.zlatanov.subsbuddy.command.delete;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeleteFileCommand {

	private String	id;
}

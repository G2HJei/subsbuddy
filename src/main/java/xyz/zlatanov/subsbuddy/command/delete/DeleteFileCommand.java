package xyz.zlatanov.subsbuddy.command.delete;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeleteFileCommand {

	private UUID id;
}

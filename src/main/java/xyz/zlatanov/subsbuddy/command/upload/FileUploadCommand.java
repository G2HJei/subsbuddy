package xyz.zlatanov.subsbuddy.command.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Getter(onMethod = @__(@JsonProperty)) // allow (de)serialization of fluent accessors
public class FileUploadCommand {

	private String	filename;
	private String	content;
	private String	owner;
}

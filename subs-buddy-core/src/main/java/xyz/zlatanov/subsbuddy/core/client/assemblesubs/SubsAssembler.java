package xyz.zlatanov.subsbuddy.core.client.assemblesubs;

import java.util.List;

import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public interface SubsAssembler {

	String assemble(List<SubtitleEntry> entries);
}

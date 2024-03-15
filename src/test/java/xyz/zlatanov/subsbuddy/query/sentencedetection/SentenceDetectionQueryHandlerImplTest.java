package xyz.zlatanov.subsbuddy.query.sentencedetection;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lombok.val;

class SentenceDetectionQueryHandlerImplTest {

	SentenceDetectionQueryHandlerImpl handler = new SentenceDetectionQueryHandlerImpl();

	@Test
	void execute_singleSentence_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery().text("Happy Rusev day.")).sentenceList();
		assertEquals(1, sentenceList.size());
		assertEquals("Happy Rusev day.", sentenceList.getFirst());
	}

	@Test
	void execute_twoSentenceText_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery().text("Happy Rusev day. Rusev machka!")).sentenceList();
		assertEquals(2, sentenceList.size());
		assertEquals("Happy Rusev day.", sentenceList.getFirst());
		assertEquals("Rusev machka!", sentenceList.getLast());
	}

	@Test
	void execute_sentenceWithTitle_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery().text("Mr. Anderson, surprised to see me?")).sentenceList();
		assertEquals(1, sentenceList.size());
		assertEquals("Mr. Anderson, surprised to see me?", sentenceList.getFirst());
	}

	@Test
	void execute_textWithPartialSentence_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery().text("nuke them from orbit. Just to be sure.")).sentenceList();
		assertEquals(2, sentenceList.size());
		assertEquals("nuke them from orbit.", sentenceList.getFirst());
		assertEquals("Just to be sure.", sentenceList.getLast());
	}

	@Test
	void execute_threeSentencesText_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery()
				.text("nuke them from orbit. Just to be sure. Alright.")).sentenceList();
		assertEquals(3, sentenceList.size());
		assertEquals("nuke them from orbit.", sentenceList.getFirst());
		assertEquals("Just to be sure.", sentenceList.get(1));
		assertEquals("Alright.", sentenceList.getLast());
	}

	@Test
	void execute_dialogue_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery().text("Luke, I am your father! -No!")).sentenceList();
		assertEquals(2, sentenceList.size());
		assertEquals("Luke, I am your father!", sentenceList.getFirst());
		assertEquals("-No!", sentenceList.getLast());
	}

	@Test
	void execute_noSeparationSentences_detects() {
		val sentenceList = handler.execute(new SentenceDetectionQuery().text("Happy Rusev day.Rusev machka!")).sentenceList();
		assertEquals(2, sentenceList.size());
		assertEquals("Happy Rusev day.", sentenceList.getFirst());
		assertEquals("Rusev machka!", sentenceList.getLast());
	}
}
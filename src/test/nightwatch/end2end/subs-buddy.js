const fs = require('fs');
const path = require('path');

describe('Subs Buddy Test Suite', function () {

	const over1MbFile = path.join(__dirname, 'generated_file.srt');
	const nonSrtFile = path.join(__dirname, 'generated_file.txt');
	const nonEnglishFile = path.join(__dirname, 'non_english.srt');
	const englishFile = path.join(__dirname, 'e2e.srt');

	before(function (browser) {
		fs.writeFileSync(over1MbFile, Buffer.alloc(1024 * 100 + 1)); // 100KB + 1 byte
		fs.writeFileSync(nonSrtFile, Buffer.alloc(1));
		fs.writeFileSync(nonEnglishFile, 'Неанглийски файл.');
		fs.writeFileSync(englishFile,
			'1\n' +
			'00:00:00,000 --> 00:00:10,000\n' +
			'Very English isn\'t it?');
		browser.navigateTo(process.env.SUBS_BUDDY_URL);
	});

	it('Test title of the page', function (browser) {
		browser.assert.titleEquals('Subs Buddy');
	});

	it('Reject non-srt files', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', nonSrtFile);
		browser.click('button#btnUpload');
		browser.expect.element('p.alert.alert-danger').text.to.equal('Please select .srt file.');
	});

	it('Reject files over 100KB', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', over1MbFile);
		browser.click('button#btnUpload');
		browser.expect.element('p.alert.alert-danger').text.to.equal('File too big. (max 100KB)');
	});

	it('Reject non-English files', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', nonEnglishFile);
		browser.click('button#btnUpload');
		browser.expect.element('p.alert.alert-danger').text.to.equal('Unsupported language detected. Supported languages: English.');
	});

	it('Upload English sub', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', englishFile);
		browser.click('button#btnUpload');
		browser.expect(browser.element.findByText('e2e.srt')).to.be.present;
	});

	it('Delete English sub', function (browser) {
		browser.element('.bi-file-earmark-x').click();
	});

	it('No files remain', function (browser) {
		browser.expect.elements('.e2e-test').count.to.equal(0);
	});

	after(function (browser) {
		browser.end(() => {
			[over1MbFile, nonSrtFile, nonEnglishFile, englishFile]
				.forEach(filePath => {
					if (fs.existsSync(filePath)) {
						fs.unlinkSync(filePath);
					}
				});
		});
	});
})
;
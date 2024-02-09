const fs = require('fs');
const path = require('path');

describe('Subs Buddy Test Suite', function () {

	const dirPath = path.join(__dirname, 'generatedFiles');
	const over1MbFile = path.join(__dirname, 'generatedFiles', 'generated_file.srt');
	const nonSrtFile = path.join(__dirname, 'generatedFiles', 'generated_file.txt');

	before(function (browser) {
		// Generate a file with size just over 1 MB
		if (!fs.existsSync(dirPath)) {
			fs.mkdirSync(dirPath, {recursive: true});
		}
		fs.writeFileSync(over1MbFile, Buffer.alloc(1048577)); // 1 MB + 1 byte
		fs.writeFileSync(nonSrtFile, Buffer.alloc(1));

		browser.navigateTo(process.env.SUBS_BUDDY_URL);
	});

	it('tests title of the page', function (browser) {
		browser.assert.titleEquals('Subs Buddy');
	});

	it('Reject files over 100KB', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', over1MbFile);
		browser.click('button#btnUpload');
		browser.expect.element('p.alert.alert-danger').text.to.equal('File too big. (max 100KB)');
	});

	it('Reject non-srt files', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', nonSrtFile);
		browser.click('button#btnUpload');
		browser.expect.element('p.alert.alert-danger').text.to.equal('Please select .srt file.');
	})

	after(function (browser) {
		browser.end(() => {
			if (fs.existsSync(over1MbFile)) {
				fs.unlinkSync(over1MbFile);
			}
			if (fs.existsSync(nonSrtFile)) {
				fs.unlinkSync(nonSrtFile);
			}
			fs.rmdirSync(dirPath);
		});
	});
});
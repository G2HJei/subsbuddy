const fs = require('fs');
const path = require('path');

describe('Subs Buddy Test Suite', function () {

	const dirPath = path.join(__dirname, '../generatedFiles');
	const over1MbFile = path.join(__dirname, '../generatedFiles', 'generated_file.srt');

	before(function (browser) {
		// Generate a file with size just over 1 MB
		if (!fs.existsSync(dirPath)) {
			fs.mkdirSync(dirPath, {recursive: true});
		}
		const data = Buffer.alloc(1048577); // 1 MB + 1 byte
		fs.writeFileSync(over1MbFile, data);

		browser.navigateTo(process.env.SUBS_BUDDY_URL);
	});

	it('tests title of the page', function (browser) {
		browser.assert.titleEquals('Subs Buddy');
	});

	it('Reject files over 1MB', function (browser) {
		browser.setValue('input#srtSelect[type="file"]', over1MbFile);
		browser.click('button#btnUpload');
		browser.expect.element('p.alert.alert-danger').text.to.equal('File too big. (max 1MB)');
	});


	after(function (browser) {
		browser.end(() => {
			if (fs.existsSync(over1MbFile)) {
				fs.unlinkSync(over1MbFile);
			}
		});
	});
});
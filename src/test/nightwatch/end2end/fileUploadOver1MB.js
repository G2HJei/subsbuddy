const fs = require('fs');
const path = require('path');

describe('Reject files over 1MB', function () {

	const dirPath = path.join(__dirname, '../generatedFiles');
	const filePath = path.join(__dirname, '../generatedFiles', 'generated_file.srt');

	before(function (browser) {
		browser.navigateTo(process.env.SUBS_BUDDY_URL);
		// Generate a file with size just over 1 MB
		if (!fs.existsSync(dirPath)) {
			fs.mkdirSync(dirPath, {recursive: true});
		}
		const data = Buffer.alloc(1048577); // 1 MB + 1 byte
		fs.writeFileSync(filePath, data);

		// Set the file path to the file input field
		browser.setValue('input#srtSelect[type="file"]', filePath);

		// Click on the upload button
		browser.click('button#btnUpload');
	});

	it('tests error message', function (browser) {
		browser.expect.element('p.alert.alert-danger').text.to.equal('File too big. (max 1MB)');
	});


	after(function (browser) {
		browser.end(() => {
			if (fs.existsSync(filePath)) {
				fs.unlinkSync(filePath);
			}
		});
	});
});

function generateFile(filePath, sizeInBytes) {

}
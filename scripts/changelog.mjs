import { execFileSync } from 'node:child_process';

const ignoredAuthors = ['PhoenixofForce'];

function findFirstCommit() {
	try {
		const headTag = execFileSync('git', ['tag', '--points-at', 'HEAD'], {
			encoding: 'utf8'
		}).trim();
		if (headTag) {
			return execFileSync('git', ['describe', '--tags', '--abbrev=0', headTag + '^'], {
				encoding: 'utf8'
			}).trim();
		}
		return execFileSync('git', ['describe', '--tags', '--abbrev=0'], {
			encoding: 'utf8',
			stdio: ['ignore', 'pipe', 'ignore']
		}).trim();
	} catch (_) {
		return undefined;
	}
}

const firstCommit = findFirstCommit();
function logCategory(trailerKey, categoryTitle) {
	const logs = execFileSync(
		'git',
		[
			'log',
			firstCommit ? firstCommit + '..' + 'HEAD' : undefined,
			'--reverse',
			'--format=%h%x01%(trailers:key=' + trailerKey + ',valueonly,unfold,separator=%x00)%x01%an'
		].filter((e) => e !== undefined),
		{ encoding: 'utf8' }
	)
		.trim()
		.split('\n');

	const logLines = [];
	for (const line of logs) {
		const params = line.split('\u0001');
		if (params.length < 3) continue;

		const hash = params[0];
		const messages = params[1].split('\u0000');
		const author = params[2];
		const parsedAuthor = ignoredAuthors.includes(author) ? '' : ` @by ${author}`;

		if (!messages.length || (messages.length === 1 && !messages[0].length)) continue;
		for (const message of messages) {
			logLines.push(`- ${message}${parsedAuthor} in ${hash}`);
		}
	}

	if (!logLines.length) return;
	console.log(`## ${categoryTitle}`);
	logLines.forEach((logLine) => console.log(logLine));
	console.log();
}

logCategory('Changelog-breaking', 'Breaking changes');
logCategory('Changelog-feature', 'Features');
logCategory('Changelog-enhancement', 'Enhancements');
logCategory('Changelog-fix', 'Bugfixes');

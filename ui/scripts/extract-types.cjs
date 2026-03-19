const fs = require('fs');
const path = require('path');

const schemaPath = path.resolve(__dirname, '../src/lib/api/schema.d.ts');
const outputPath = path.resolve(__dirname, '../src/lib/api/types.ts');

const content = fs.readFileSync(schemaPath, 'utf-8');

const schemasStart = content.indexOf('schemas: {');
if (schemasStart === -1) {
	console.error('No schemas found in schema.d.ts');
	process.exit(1);
}

// Only take from "schemas: {" until the closing "};" at 4-space indent
const schemaSection = content.slice(schemasStart, content.indexOf('\n    };', schemasStart));
const typeNames = [...schemaSection.matchAll(/^ {8}(\w+): \{/gm)].map((m) => m[1]);

const lines = [
	'// Auto-generated — do not edit manually.',
	'// Run "npm run generate:api" to regenerate.',
	"import type { components } from './schema';",
	''
];

for (const name of typeNames) {
	lines.push(`export type ${name} = components['schemas']['${name}'];`);
}

lines.push('');

fs.writeFileSync(outputPath, lines.join('\n'));
console.log(`Generated ${typeNames.length} types: ${typeNames.join(', ')}`);
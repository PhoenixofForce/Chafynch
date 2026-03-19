// Downloads Natural Earth 50m countries + 10m provinces, saves one JSON per country.
// Run: node scripts/generate-countries.cjs
// Output: static/countries/DE.json, static/countries/CN.json, etc.

const fs = require('fs');
const path = require('path');

const COUNTRIES_URL =
	'https://raw.githubusercontent.com/nvkelso/natural-earth-vector/master/geojson/ne_10m_admin_0_countries.geojson';
const PROVINCES_URL =
	'https://raw.githubusercontent.com/nvkelso/natural-earth-vector/master/geojson/ne_10m_admin_1_states_provinces.geojson';

function project(lon, lat) {
	const x = (lon + 180) / 360;
	const latClamped = Math.max(-85, Math.min(85, lat));
	const latRad = (latClamped * Math.PI) / 180;
	const y = 0.5 - Math.log(Math.tan(Math.PI / 4 + latRad / 2)) / (2 * Math.PI);
	return [Math.round(x * 1000 * 100) / 100, Math.round(y * 1000 * 100) / 100];
}

function toSvgPath(geometry) {
	const polys = geometry.type === 'Polygon' ? [geometry.coordinates] : geometry.coordinates;
	return polys
		.map((poly) =>
			poly
				.map((ring) => {
					const pts = ring.map(([lon, lat]) => project(lon, lat));
					return 'M' + pts.map(([x, y]) => `${x},${y}`).join('L') + 'Z';
				})
				.join('')
		)
		.join('');
}

function getBBox(svgPath) {
	const xs = [];
	const ys = [];
	for (const m of svgPath.matchAll(/([\d.]+),([\d.]+)/g)) {
		xs.push(+m[1]);
		ys.push(+m[2]);
	}
	return { minX: Math.min(...xs), minY: Math.min(...ys), maxX: Math.max(...xs), maxY: Math.max(...ys) };
}

function bboxOverlaps(a, b) {
	return a.maxX >= b.minX && a.minX <= b.maxX && a.maxY >= b.minY && a.minY <= b.maxY;
}

async function main() {
	const outDir = path.join(__dirname, '..', 'static', 'countries');
	fs.mkdirSync(outDir, { recursive: true });

	console.log('Downloading countries (50m)...');
	const countriesGeo = await (await fetch(COUNTRIES_URL)).json();

	console.log('Downloading provinces (10m)...');
	const provincesGeo = await (await fetch(PROVINCES_URL)).json();

	// Build country data with bounding boxes
	const countries = {};
	for (const f of countriesGeo.features) {
		const props = f.properties;
		const code = props.ISO_A2_EH !== '-99' ? props.ISO_A2_EH : props.ISO_A2;
		if (code === '-99') continue;
		const svgPath = toSvgPath(f.geometry);
		if (!svgPath) continue;
		countries[code] = { name: props.NAME, path: svgPath, bbox: getBBox(svgPath) };
	}

	// Group provinces by country code
	const provinces = {};
	for (const f of provincesGeo.features) {
		const code = f.properties.iso_a2;
		if (!code || code === '-99') continue;
		const svgPath = toSvgPath(f.geometry);
		if (!svgPath) continue;
		if (!provinces[code]) provinces[code] = [];
		provinces[code].push(svgPath);
	}

	// Find neighbors per country (bounding box overlap with padding)
	function findNeighbors(targetCode) {
		const bbox = countries[targetCode].bbox;
		const padX = (bbox.maxX - bbox.minX) * 0.5;
		const padY = (bbox.maxY - bbox.minY) * 0.5;
		const expanded = {
			minX: bbox.minX - padX, minY: bbox.minY - padY,
			maxX: bbox.maxX + padX, maxY: bbox.maxY + padY
		};
		return Object.keys(countries).filter(
			(code) => code !== targetCode && bboxOverlaps(countries[code].bbox, expanded)
		);
	}

	// Write one JSON per country
	let count = 0;
	for (const [code, data] of Object.entries(countries)) {
		const json = {
			name: data.name,
			path: data.path,
			provinces: provinces[code] || [],
			neighbors: findNeighbors(code)
		};
		fs.writeFileSync(path.join(outDir, `${code}.json`), JSON.stringify(json));
		count++;
	}

	// Write index mapping country name → code
	const index = Object.entries(countries)
		.map(([code, data]) => ({ code, name: data.name }))
		.sort((a, b) => a.name.localeCompare(b.name));
	fs.writeFileSync(path.join(outDir, 'index.json'), JSON.stringify(index));

	// Clean up old single file if it exists
	const oldFile = path.join(__dirname, '..', 'src', 'lib', 'data', 'countries.json');
	if (fs.existsSync(oldFile)) fs.unlinkSync(oldFile);

	console.log(`Written ${count} country files + index.json to ${outDir}`);
}

main();
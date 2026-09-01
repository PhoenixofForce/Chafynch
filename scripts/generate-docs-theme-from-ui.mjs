import { readFileSync, writeFileSync } from "node:fs";

function generateMap(path) {
	const style = readFileSync(path).toString();
	const map = [];

	for (let line of style.split("\n")) {
		if (!(line.trim().startsWith("--") && line.includes(":"))) {
			continue;
		}

		const cssVar = line.trim().split(":")[0];
		const cssVal = line.trim().split(":")[1].split(";")[0];
		map[cssVar] = cssVal;
	}
	return map;
}

function generateCss(map, themeName) {
	let style = readFileSync("./docs/src/styles/custom.in.css").toString();

	style = style.replace("--theme", themeName);
	for (let themeVar of Object.keys(map).sort((a, b) => b.length - a.length)) {
		style = style.replaceAll(themeVar, map[themeVar]);
	}

	return style;
}

const dark = generateMap("./ui/src/routes/dark.css");
const light = generateMap("./ui/src/routes/light.css");
const finalTheme = generateCss(dark, "dark") + generateCss(light, "light");
writeFileSync("./docs/src/styles/custom.gen.css", finalTheme);

let promise: Promise<Record<string, string>> | null = null;

export function getCountryCodeMap(): Promise<Record<string, string>> {
	if (!promise) {
		promise = fetch('/countries/index.json')
			.then((res) => (res.ok ? res.json() : []))
			.then((entries: { code: string; name: string }[]) =>
				Object.fromEntries(entries.map((e) => [e.name, e.code]))
			);
	}
	return promise;
}

export interface CountryData {
	name: string;
	path: string;
	provinces: string[];
	neighbors: string[];
}

const countryCache = new Map<string, Promise<CountryData | null>>();
export function getCountryData(code: string): Promise<CountryData | null> {
	if (!countryCache.has(code)) {
		countryCache.set(
			code,
			fetch(`/countries/${code}.json`).then((res) => (res.ok ? res.json() : null))
		);
	}
	return countryCache.get(code)!;
}

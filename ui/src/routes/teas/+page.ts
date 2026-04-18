import { api } from '$lib/api/client';

export async function load() {
	const { data: teas } = await api.GET('/api/teas');
	const { data: types } = await api.GET('/api/tea-types');

	const countries = unique(teas?.map((e) => e.originCountry).filter((e) => !!e) ?? []);

	return { teas: teas ?? [], types, countries };
}

function unique<T>(array: T[]): T[] {
	return [...new Set(array)];
}

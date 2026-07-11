import { api } from '$lib/api/client';

export async function load() {
	const { data: types } = await api.GET('/api/tea-types');
	return { types };
}

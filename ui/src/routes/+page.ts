import { api } from '$lib/api/client';

export async function load() {
	const { data } = await api.GET('/api/teas');
	return { teas: data ?? [] };
}

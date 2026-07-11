import { api } from '$lib/api/client';

export async function load() {
	const { data: cultivars } = await api.GET('/api/cultivars');
	return { cultivars };
}

import { api } from '$lib/api/client';

export async function load() {
	const { data: vendors } = await api.GET('/api/vendors');
	return { vendors };
}

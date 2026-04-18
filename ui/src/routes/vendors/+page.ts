import { api } from '$lib/api/client';

export async function load() {
	const { data: vendors } = await api.GET('/api/vendors');
	vendors?.forEach((v) => {
		if (v.vendor.locationDto) return;
		v.vendor.locationDto = { country: '', province: '', city: '' };
	});
	return { vendors };
}

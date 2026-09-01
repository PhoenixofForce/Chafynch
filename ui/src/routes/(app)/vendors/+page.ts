import { vendorService } from '$lib/api/vendor.service';

export async function load() {
	const vendors = await vendorService.getAll();
	vendors?.forEach((v) => {
		if (v.vendor.locationDto) return;
		v.vendor.locationDto = { country: '', province: '', city: '' };
	});
	return { vendors };
}

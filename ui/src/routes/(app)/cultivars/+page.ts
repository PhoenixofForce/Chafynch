import { cultivarService } from '$lib/api/cultivar.service';

export async function load() {
	const cultivars = await cultivarService.getAll();
	return { cultivars };
}

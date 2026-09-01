import { extractionService } from '$lib/api/extraction.service';

export async function load() {
	const profiles = await extractionService.getAll();
	return { profiles };
}

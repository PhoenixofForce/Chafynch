import { teaTypeService } from '$lib/api/teaType.service';

export async function load() {
	const types = await teaTypeService.getAll();
	return { types };
}

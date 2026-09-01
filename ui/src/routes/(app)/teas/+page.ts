import { teaService } from '$lib/api/tea.service';
import { teaTypeService } from '$lib/api/teaType.service';

export async function load() {
	const teas = await teaService.getAll();
	const types = await teaTypeService.getAll();

	const countries = unique(teas.map((e) => e.originCountry).filter((e) => !!e));

	return { teas: teas, types, countries };
}

function unique<T>(array: T[]): T[] {
	return [...new Set(array)];
}

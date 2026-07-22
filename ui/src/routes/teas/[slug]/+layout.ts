import { teaService } from '$lib/api/tea.service.js';
import { type Breadcrumb } from '$lib/layout/routes';

export async function load({ params, parent }) {
	const { breadcrumbs } = await parent();
	const slug = parseInt(params.slug);
	const tea = await teaService.getById(slug);

	return {
		tea: tea,
		breadcrumbs: [
			...breadcrumbs,
			{ label: tea.name, path: `/teas/${slug}` }
		] satisfies Breadcrumb[]
	};
}

import { api } from '$lib/api/client';
import { type Breadcrumb } from '$lib/layout/routes';

export async function load({ params, parent }) {
	const { breadcrumbs } = await parent();
	const slug = parseInt(params.slug);
	const { data: tea } = await api.GET('/api/teas/{id}', { params: { path: { id: slug } } });

	return {
		tea: tea!,
		breadcrumbs: [
			...breadcrumbs,
			{ label: tea!.name, path: `/teas/${slug}` }
		] satisfies Breadcrumb[]
	};
}

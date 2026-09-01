import { breadcrumbFor, type Breadcrumb } from '$lib/layout/routes';

export async function load() {
	return {
		breadcrumbs: [breadcrumbFor('/teas')!] satisfies Breadcrumb[]
	};
}

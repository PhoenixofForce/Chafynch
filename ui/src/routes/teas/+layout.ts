import { breadcrumbFor, type Breadcrumb } from '$lib/routes';

export async function load() {
	return {
		breadcrumbs: [breadcrumbFor('/teas')!] satisfies Breadcrumb[]
	};
}

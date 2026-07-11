import { breadcrumbFor, type Breadcrumb } from '$lib/routes';

export const ssr = false;

export async function load() {
	return {
		breadcrumbs: [breadcrumbFor('/cultivars')!] satisfies Breadcrumb[]
	};
}

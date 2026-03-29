import type { Breadcrumb } from '$lib/routes';

export const ssr = false;

export async function load({ parent }) {
	const { breadcrumbs } = await parent();
	return {
		breadcrumbs: [...breadcrumbs, { label: 'Vendors', path: '/vendors' }] satisfies Breadcrumb[]
	};
}

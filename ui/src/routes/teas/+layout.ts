import type { Breadcrumb } from '$lib/routes';
import { Leaf } from '@lucide/svelte';

export async function load({ parent }) {
	const { breadcrumbs } = await parent();
	return {
		breadcrumbs: [
			...breadcrumbs,
			{ label: 'Teas', path: '/teas', icon: Leaf }
		] satisfies Breadcrumb[]
	};
}

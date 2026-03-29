import type { Breadcrumb } from '$lib/routes';
import { Plus } from '@lucide/svelte';

export async function load({ parent }) {
	const { breadcrumbs } = await parent();
	return {
		breadcrumbs: [
			...breadcrumbs,
			{ label: 'Add Tea', path: '/teas/new', icon: Plus }
		] satisfies Breadcrumb[]
	};
}

import type { Breadcrumb } from '$lib/routes';
import { House } from '@lucide/svelte';

export const ssr = false;

export async function load() {
	return {
		breadcrumbs: [{ label: 'Home', path: '/', icon: House }] satisfies Breadcrumb[]
	};
}

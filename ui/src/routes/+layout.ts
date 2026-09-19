import { breadcrumbFor, type Breadcrumb } from '$lib/layout/routes';
import { loadLocale } from 'wuchale/load-utils';
import '../locales/main.loader.svelte.js';

export const ssr = false;
export async function load() {
	await loadLocale(localStorage.getItem('lang') ?? 'en');

	return {
		breadcrumbs: [breadcrumbFor('/')!] satisfies Breadcrumb[]
	};
}

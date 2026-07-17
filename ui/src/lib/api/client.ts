import createClient from 'openapi-fetch';
import type { paths } from './schema';
import { error, isHttpError } from '@sveltejs/kit';
import { toast } from '$lib/data/toast.svelte';
import { invalidateAll } from '$app/navigation';

export const api = createClient<paths>({
	baseUrl: ''
});

api.use({
	async onResponse({ response }) {
		if (response.ok) {
			return;
		}
		const text = await response.clone().text();
		const body: App.Error = text
			? JSON.parse(text)
			: {
					error: response.statusText,
					message: response.statusText,
					timestamp: new Date().toISOString(),
					path: new URL(response.url).pathname
				};
		throw error(response.status, body);
	}
});

export async function wrapApi<T>(
	caller: () => Promise<T>,
	options: { success?: string; error?: string; invalidate?: boolean } = {}
) {
	try {
		const out = await caller();
		if (options?.invalidate ?? true) await invalidateAll();
		if (options.success) toast.success(options.success);
		return out;
	} catch (e) {
		handleApiError(e, options.error);
		throw e;
	}
}

export function handleApiError(error: unknown, fallback = '') {
	console.log(error);
	toast.error(isHttpError(error) ? (error.body.message ?? fallback) : fallback);
}

import createClient from 'openapi-fetch';
import type { paths } from './gen/schema';
import { error } from '@sveltejs/kit';
import { invalidateAll } from '$app/navigation';
import { toast } from '$lib/toast/toast.store.svelte';

export const api = createClient<paths>({
	baseUrl: ''
});

const MUTATING = new Set(['POST', 'PUT', 'PATCH', 'DELETE']);

api.use({
	async onResponse({ request, response }) {
		if (!response.ok) {
			const text = await response.clone().text();
			const body: App.Error = text
				? JSON.parse(text)
				: {
						error: response.statusText,
						message: response.statusText,
						timestamp: new Date().toISOString(),
						path: new URL(response.url).pathname
					};

			console.error(body);
			toast.error(body.message ?? response.statusText);
			throw error(response.status, body);
		}

		if (MUTATING.has(request.method)) await invalidateAll();
	}
});

export async function unwrap<T>(call: Promise<{ data?: T }>): Promise<T> {
	const { data } = await call;
	return data as T;
}

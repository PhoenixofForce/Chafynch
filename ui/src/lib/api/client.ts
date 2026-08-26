import createClient from 'openapi-fetch';
import type { paths } from './gen/schema';
import { error } from '@sveltejs/kit';
import { invalidateAll } from '$app/navigation';
import { toast } from '$lib/toast/toast.store.svelte';

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

		console.error(body);
		toast.error(body.message ?? response.statusText);
		throw error(response.status, body);
	}
});

export async function unwrap<T>(call: Promise<{ data?: T }>): Promise<T> {
	const { data } = await call;
	return data as T;
}

export async function mutate<T>(call: Promise<{ data?: T }>): Promise<T> {
	const out = await unwrap(call);
	await invalidateAll();
	return out;
}

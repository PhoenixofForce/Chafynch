import createClient from 'openapi-fetch';
import type { paths } from './schema';
import { env } from '$env/dynamic/public';
import { error } from '@sveltejs/kit';

export const api = createClient<paths>({
	baseUrl: env.PUBLIC_BACKEND_URL
});

api.use({
	async onResponse({ response }) {
		if (!response.ok) {
			const body = await response.clone().json();
			throw error(body.status, JSON.stringify(body));
		}
	}
});

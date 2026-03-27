import createClient from 'openapi-fetch';
import type { paths } from './schema';
import { error } from '@sveltejs/kit';

export const api = createClient<paths>({
	baseUrl: ''
});

api.use({
	async onResponse({ response }) {
		if (!response.ok) {
			const body = await response.clone().json();
			throw error(body.status, body as App.Error);
		}
	}
});

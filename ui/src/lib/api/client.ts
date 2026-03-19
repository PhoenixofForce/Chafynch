import createClient from 'openapi-fetch';
import type { paths } from './schema';

export const api = createClient<paths>({
	baseUrl: 'http://192.168.178.75:8080'
});
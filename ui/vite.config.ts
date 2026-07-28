import devtoolsJson from 'vite-plugin-devtools-json';
import tailwindcss from '@tailwindcss/vite';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vitest/config';
import { svelteTesting } from '@testing-library/svelte/vite';

export default defineConfig({
	plugins: [tailwindcss(), sveltekit(), devtoolsJson(), svelteTesting()],
	server: {
		proxy: {
			'/api': {
				target: 'http://localhost:8080'
			}
		}
	},
	test: {
		environment: 'jsdom',
		setupFiles: ['./src/vitest-setup.js']
	},
	resolve: process.env.VITEST ? { conditions: ['browser'] } : undefined
});

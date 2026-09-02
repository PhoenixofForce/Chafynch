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
			},
			'/docs': {
				target: 'http://localhost:4322',
				ws: true
			},
			'/_astro': {
				target: 'http://localhost:4322',
				ws: true
			}
		}
	},
	test: {
		environment: 'jsdom',
		setupFiles: ['./src/vitest-setup.js'],
		deps: {
			optimizer: {
				client: {
					enabled: true,
					include: [
						'@lucide/svelte',
						'svelte',
						'svelte/internal/client',
						'svelte/internal/disclose-version'
					]
				}
			}
		}
	},
	resolve: process.env.VITEST ? { conditions: ['browser'] } : undefined
});

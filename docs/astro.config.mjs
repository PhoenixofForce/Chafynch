// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	integrations: [
		starlight({
			title: 'Chafynch',
			customCss: ['./src/styles/custom.gen.css'],
			social: [
				{
					icon: 'github',
					label: 'GitHub',
					href: 'https://github.com/PhoenixofForce/Chafynch/'
				}
			],
			sidebar: [
				{
					label: 'Install',
					items: [{ autogenerate: { directory: 'docs/install' } }]
				},
				{
					label: 'Features',
					items: [{ autogenerate: { directory: 'docs/features' } }]
				}
			],
			editLink: {
				baseUrl: 'https://github.com/phoenixofforce/Chafynch/edit/main/docs/'
			}
		})
	]
});

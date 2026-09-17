import { adapter as svelte, svelteKitDefaultHeuristic } from '@wuchale/svelte';
import { defineConfig } from 'wuchale';

const excludedAttributes = ['class', 'style', 'onkeydown'];

export default defineConfig({
	locales: ['en', 'de'],
	adapters: {
		main: svelte({
			loader: 'sveltekit',
			heuristic: (txt, file) => {
				if (
					txt.path.some(
						(s) =>
							s.type === 'attribute' &&
							excludedAttributes.some((attribute) => s.name.toLowerCase().endsWith(attribute))
					)
				) {
					return false;
				}

				return svelteKitDefaultHeuristic(txt, file);
			},
		})
	}
});

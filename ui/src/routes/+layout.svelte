<script lang="ts">
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import { loadLocale } from 'wuchale/load-utils';
	import '../locales/main.loader.svelte.js';
	import Controls from '$lib/layout/ThemeToggle.svelte';

	let locale = $state('de');
	$effect(() => {
		loadLocale(locale);
	});

	let { children } = $props();
</script>

<svelte:head><link href={favicon} rel="icon" /></svelte:head>
<button class="btn" onclick={() => (locale == 'de' ? (locale = 'en') : (locale = 'de'))}>
	sprache {locale}
</button>
{@render children()}
<Controls />

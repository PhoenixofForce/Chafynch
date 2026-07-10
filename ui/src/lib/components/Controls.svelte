<script lang="ts">
	import { Moon, Sun } from '@lucide/svelte';
	import Swap from './ui/Swap.svelte';

	const saved = localStorage.getItem('theme');
	let isDark = $state(saved === 'forest');
	if (saved) {
		document.documentElement.setAttribute('data-theme', saved);
	}

	function toggleDarkMode() {
		isDark = !isDark;
		const theme = isDark ? 'forest' : 'lemonade';
		localStorage.setItem('theme', theme);
		document.documentElement.classList.add('no-transitions');
		document.documentElement.setAttribute('data-theme', theme);
		requestAnimationFrame(() => {
			document.documentElement.classList.remove('no-transitions');
		});
	}
</script>

<div class="fixed right-4 bottom-4 z-50 flex flex-row gap-4">
	<Swap value={isDark} onchange={toggleDarkMode}>
		{#snippet on()}
			<Moon />
		{/snippet}
		{#snippet off()}
			<Sun />
		{/snippet}
	</Swap>
</div>

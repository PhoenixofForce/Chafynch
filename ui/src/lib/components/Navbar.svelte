<script>
	import { resolve } from '$app/paths';
	import { PanelLeftClose, PanelLeftOpen } from '@lucide/svelte';

	let { drawerOpen } = $props();
</script>

<nav class="navbar w-full bg-base-300 shadow-sm">
	<label
		for="sidebar"
		aria-label="open sidebar"
		class="btn swap btn-square opacity-70 btn-ghost transition-all hover:scale-110 hover:opacity-100"
	>
		<!-- Sidebar toggle icon -->
		<input type="checkbox" bind:checked={drawerOpen} />
		<div class="swap-on"><PanelLeftClose class="is-drawer-close:hidden" /></div>
		<div class="swap-off"><PanelLeftOpen class="is-drawer-close:hidden" /></div>
	</label>
	<a href={resolve('/')} class="btn text-xl btn-ghost">Tea Tracker</a>
</nav>

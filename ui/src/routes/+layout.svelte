<script lang="ts">
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import Navbar from '$lib/components/Navbar.svelte';
	import Sidebar from '$lib/components/Sidebar.svelte';

	let { children } = $props();
	let drawerOpen = $state(false);
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>
<div class="drawer lg:drawer-open">
	<input id="sidebar" type="checkbox" bind:checked={drawerOpen} class="drawer-toggle" />
	<div class="drawer-content">
		<!-- Navbar -->
		<Navbar {drawerOpen} />
		<!-- Page content here -->
		{@render children()}
	</div>

	<div class="drawer-side is-drawer-close:overflow-visible">
		<label for="sidebar" aria-label="close sidebar" class="drawer-overlay"></label>
		<div
			class="flex min-h-full min-w-15 flex-col items-start bg-base-200 is-drawer-close:w-15 is-drawer-open:w-64"
		>
			<!-- Sidebar content here -->
			<Sidebar />
		</div>
	</div>
</div>

<script lang="ts">
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import Navbar from '$lib/components/Navbar.svelte';
	import { page } from '$app/state';
	import Toast from '$lib/components/ui/Toast.svelte';
	import Sidebar from '$lib/components/ui/Sidebar.svelte';
	import Controls from '$lib/components/Controls.svelte';
	import SidebarMenu from '$lib/components/SidebarMenu.svelte';

	let { children } = $props();
	let drawerOpen = $state(false);
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

<div class="w-full">
	<!-- Navbar -->
	<Navbar breadcrumbs={page.data.breadcrumbs} bind:drawerOpen />
	<Sidebar bind:drawerOpen>
		{#snippet pageContent()}
			<div class="mx-auto w-full max-w-5xl p-6">
				{@render children()}
			</div>
		{/snippet}

		{#snippet sidebarContent()}
			<SidebarMenu />
		{/snippet}
	</Sidebar>

	<Toast />
	<Controls />
</div>

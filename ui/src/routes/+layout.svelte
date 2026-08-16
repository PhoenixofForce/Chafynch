<script lang="ts">
	import './layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import Navbar from '$lib/layout/Navbar.svelte';
	import { page } from '$app/state';
	import Toast from '$lib/toast/Toast.svelte';
	import Sidebar from '$lib/basics/Sidebar.svelte';
	import Controls from '$lib/layout/ThemeToggle.svelte';
	import SidebarMenu from '$lib/layout/SidebarMenu.svelte';
	import ConfirmationModal from '$lib/confirmation/ConfirmationModal.svelte';

	let { children } = $props();
	let drawerOpen = $state(false);
</script>

<svelte:head><link href={favicon} rel="icon" /></svelte:head>

<div class="flex h-svh w-full flex-col">
	<!-- Navbar -->
	<Navbar breadcrumbs={page.data.breadcrumbs} bind:drawerOpen />
	<Sidebar bind:drawerOpen>
		{#snippet pageContent()}
			<div
				class="mx-auto flex w-full max-w-6xl flex-1 flex-col items-center"
				class:min-h-0={page.data.selfScrolling}
			>
				{@render children()}
			</div>
		{/snippet}

		{#snippet sidebarContent()}
			<SidebarMenu />
		{/snippet}
	</Sidebar>

	<Toast />
	<ConfirmationModal />
	<Controls />
</div>

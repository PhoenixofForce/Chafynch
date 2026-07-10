<script lang="ts">
	import type { Snippet } from 'svelte';

	let {
		drawerOpen = $bindable(false),
		pageContent,
		sidebarContent
	}: {
		drawerOpen?: boolean;
		pageContent: Snippet;
		sidebarContent: Snippet;
	} = $props();
</script>

<div class="drawer lg:drawer-open">
	<input id="sidebar" type="checkbox" bind:checked={drawerOpen} class="drawer-toggle" />
	<div class="drawer-content">
		<!-- Page content here -->
		<div class="flex justify-center">
			{@render pageContent()}
		</div>
	</div>

	<div class="drawer-side is-drawer-close:overflow-visible">
		<label for="sidebar" aria-label="close sidebar" class="drawer-overlay"></label>
		<div
			class="flex min-h-full min-w-15 flex-col items-start bg-base-200 is-drawer-close:w-15 is-drawer-open:w-64"
		>
			<!-- Sidebar content here -->
			{@render sidebarContent()}
		</div>
	</div>
</div>

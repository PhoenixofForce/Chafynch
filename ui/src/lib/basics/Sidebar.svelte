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

<div class="drawer min-h-0 flex-1 lg:drawer-open">
	<input id="sidebar" class="drawer-toggle" type="checkbox" bind:checked={drawerOpen} />
	<div class="drawer-content flex flex-col overflow-y-auto p-6">
		<!-- Page content here -->
		{@render pageContent()}
	</div>

	<div class="drawer-side lg:h-auto is-drawer-close:overflow-visible">
		<label class="drawer-overlay" aria-label="close sidebar" for="sidebar"></label>
		<div
			class="flex min-h-full min-w-15 flex-col items-start bg-base-200 lg:h-auto is-drawer-close:w-15 is-drawer-open:w-64"
		>
			<!-- Sidebar content here -->
			{@render sidebarContent()}
		</div>
	</div>
</div>

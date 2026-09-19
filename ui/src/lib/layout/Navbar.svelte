<script>
	import { resolve } from '$app/paths';
	import { PanelLeftClose, PanelLeftOpen } from '@lucide/svelte';
	import Swap from '$lib/basics/Swap.svelte';
	import UserSettings from './UserSettings.svelte';

	let { drawerOpen = $bindable(), breadcrumbs } = $props();
</script>

<nav class="navbar flex w-full justify-between bg-base-300 shadow-sm">
	<div class="flex items-center">
		<Swap bind:value={drawerOpen}>
			{#snippet on()}
				<PanelLeftClose />
			{/snippet}
			{#snippet off()}
				<PanelLeftOpen />
			{/snippet}
		</Swap>
		<div class="breadcrumbs ml-2 text-sm">
			<ul>
				{#each breadcrumbs as breadcrumb, i (i)}
					<li>
						<span class="inline-flex items-center gap-2">
							<breadcrumb.icon />
							<a href={resolve(breadcrumb.path)}>{breadcrumb.label}</a>
						</span>
					</li>
				{/each}
			</ul>
		</div>
	</div>
	<UserSettings bottomRight={false} />
</nav>

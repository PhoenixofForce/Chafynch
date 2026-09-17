<script lang="ts">
	import { resolve } from '$app/paths';
	import TeaCard from './teas/TeaCard.svelte';
	import { Leaf } from '@lucide/svelte';

	let { data } = $props();
</script>

<div class="stats mt-6 w-full stats-horizontal border border-base-300 bg-base-200 shadow">
	<div class="stat">
		<div class="stat-title">Total Teas</div>
		<div class="stat-value text-primary">{data.teas.length}</div>
		<div class="stat-desc"></div>
	</div>
	<div class="stat">
		<div class="stat-title">Tea Types</div>
		<div class="stat-value">{data.types?.length ?? 0}</div>
		<div class="stat-desc">
			{data.types
				.map((e) => e.name)
				.slice(0, Math.min(3, data.types.length))
				.join(', ')}, ...
		</div>
	</div>
	<div class="stat">
		<div class="stat-title">Origin Countries</div>
		<div class="stat-value">{data.countries.length}</div>
		<div class="stat-desc">
			{data.countries.slice(0, Math.min(3, data.countries.length)).join(', ')}, ...
		</div>
	</div>
</div>

<div class="mt-6">
	<div class="flex justify-between">
		<h2 class="mb-4 text-xl font-bold text-base-content">Recent Tees</h2>
		<a class="btn btn-primary btn-sm" href={resolve('/teas')}>
			<Leaf /> All Teas
		</a>
	</div>

	{#if data.teas.length === 0}
		<p class="text-sm text-base-content/60">No teas</p>
	{:else}
		<div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
			{#each data.teas as tea (tea.id)}
				<a class="block h-full transition-all hover:scale-105" href={resolve(`/teas/${tea.id}`)}>
					<TeaCard {tea} />
				</a>
			{/each}
		</div>
	{/if}
</div>

<script lang="ts">
	import type { TeaDTO } from '$lib/api/types';
	import CountryMap from './CountryMap.svelte';
	import { Leaf, Store, Scale } from '@lucide/svelte';

	let { tea }: { tea: TeaDTO } = $props();
</script>

<div class="card border-base-300 bg-base-200 card-border">
	{#if tea.originCountry}
		<figure class="relative aspect-square overflow-hidden bg-base-300">
			<div class="h-full w-full">
				{#if tea.originCountry}
					<CountryMap
						country={tea.originCountry}
						markerLon={tea.originLongitude}
						markerLat={tea.originLatitude}
						showNeighbors={false}
					/>
				{/if}
			</div>
			<div class="absolute right-2 bottom-2 badge badge-soft badge-sm badge-secondary">
				{[tea.originCity, tea.originProvince, tea.originCountry].filter(Boolean).join(', ')}
			</div>
		</figure>
	{/if}

	<div class="card-body gap-2 p-4">
		<h3 class="card-title text-lg">{tea.name}</h3>

		<div class="flex flex-wrap items-center gap-3 text-sm text-base-content/70">
			{#if tea.teaType || tea.cultivar}
				<span class="flex items-center gap-1">
					<Leaf size="0.875rem" />
					{tea.teaType ?? ''}{tea.cultivar && tea.teaType ? ' - ' : ''}{tea.cultivar ?? ''}
				</span>
			{/if}
			{#if tea.vendor}
				<span class="flex items-center gap-1">
					<Store size="0.875rem" />
					{tea.vendor}
				</span>
			{/if}
			{#if tea.price != null && tea.weightGrams != null && tea.weightGrams > 0}
				<span class="flex items-center gap-1">
					<Scale size="0.875rem" />
					{(tea.price / tea.weightGrams).toFixed(2)} €/g
				</span>
			{/if}
		</div>
	</div>
</div>

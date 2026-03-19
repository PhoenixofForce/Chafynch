<script lang="ts">
	import { onMount } from 'svelte';
	import CountryMap from '$lib/components/CountryMap.svelte';
	import { api } from '$lib/api/client';
	import type { TeaDTO } from '$lib/api/types';
    import { Leaf, Store, Scale } from '@lucide/svelte';

	let teas = $state<TeaDTO[]>([]);
	let countryCodeMap = $state<Record<string, string>>({});

	onMount(() => {
		api.GET('/api/teas').then(({ data }) => {
			if (data) teas = data;
		});

		fetch('/countries/index.json').then(async (res) => {
			if (res.ok) {
				const entries: { code: string; name: string }[] = await res.json();
				countryCodeMap = Object.fromEntries(entries.map((e) => [e.name, e.code]));
			}
		});
	});
</script>

<div class="mx-auto max-w-5xl p-6">
	<h1 class="text-base-content text-3xl font-bold">Tea Tracker</h1>
	<div class="mt-1 flex items-center justify-between">
		<p class="text-base-content/60">Deine Tee-Sammlung im Ueberblick</p>
		<a href="/teas/new" class="btn btn-primary btn-sm">+ Neuer Tee</a>
	</div>

	<div class="stats stats-horizontal mt-6 w-full border border-base-300 bg-base-200 shadow">
		<div class="stat">
			<div class="stat-title">Tees gesamt</div>
			<div class="stat-value text-primary">42</div>
			<div class="stat-desc">+3 diese Woche</div>
		</div>
		<div class="stat">
			<div class="stat-title">Sorten</div>
			<div class="stat-value">12</div>
			<div class="stat-desc">Gruen, Schwarz, Oolong, ...</div>
		</div>
		<div class="stat">
			<div class="stat-title">Herkunftslaender</div>
			<div class="stat-value">5</div>
			<div class="stat-desc">China, Japan, Indien, ...</div>
		</div>
		<div class="stat">
			<div class="stat-title">Verkostungen</div>
			<div class="stat-value text-secondary">128</div>
			<div class="stat-desc">+12 diesen Monat</div>
		</div>
	</div>

	<div class="mt-6">
		<h2 class="text-base-content text-xl font-bold mb-4">Alle Tees</h2>
		{#if teas.length === 0}
			<p class="text-base-content/60 text-sm">Noch keine Tees vorhanden.</p>
		{:else}
			<div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
				{#each teas as tea}
					<div class="card card-border border-base-300 bg-base-200">
						{#if tea.originCountry && countryCodeMap[tea.originCountry]}
							<figure class="relative aspect-square overflow-hidden bg-base-300">
								<div class="h-full w-full">
									<CountryMap
										countryCode={countryCodeMap[tea.originCountry]}
										markerLon={tea.originLongitude}
										markerLat={tea.originLatitude}
										showNeighbors={false}
									/>
								</div>
								<div class="absolute bottom-2 left-2 badge badge-secondary text-xs">
									{[tea.originCity, tea.originProvince, tea.originCountry].filter(Boolean).join(', ')}
								</div>
							</figure>
						{/if}

						<div class="card-body gap-2 p-4">
							<h3 class="card-title text-lg">{tea.name}</h3>

							<div class="flex items-center flex-wrap gap-3 text-sm text-base-content/70">
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
				{/each}
			</div>
		{/if}
	</div>
</div>
<script lang="ts">
	import CountryMap from '$lib/components/CountryMap.svelte';

	let { data } = $props();
</script>

<div class="flex gap-4 p-8">
	Under Construction
	<div class="card min-w-80 flex-1 p-2">
		{#if data.tea.originCountry}
			<CountryMap
				country={data.tea.originCountry}
				markerLon={data.tea.originLongitude}
				markerLat={data.tea.originLatitude}
				showNeighbors={false}
			/>
		{/if}
	</div>
	<div class="prose">
		<h1>{data.tea.name}</h1>
	</div>
</div>

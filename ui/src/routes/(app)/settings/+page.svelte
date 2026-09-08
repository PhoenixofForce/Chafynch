<script lang="ts">
	import Tabs, { type TabDefinition } from '$lib/basics/Tabs.svelte';
	import ExtractionProfileTab from './ExtractionProfileTab.svelte';

	const { data } = $props();
	const tabs: TabDefinition[] = [{ id: 'extraction', label: 'Extraction Profiles' }];
</script>

<Tabs {tabs}>
	{#snippet renderTab(tabDefiniton)}
		{#if tabDefiniton.id === 'extraction'}
			<ExtractionProfileTab bind:profiles={data.profiles} />
		{/if}
	{/snippet}
</Tabs>

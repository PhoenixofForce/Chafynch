<script lang="ts">
	import ExtractionProfileTab from './ExtractionProfileTab.svelte';

	const { data } = $props();
</script>

<div class="tabs-border tabs w-full">
	<input
		name="my_tabs_2"
		class="tab checked:text-primary"
		aria-label="Scraper Settings"
		checked={true}
		type="radio"
	/>
	<div class="tab-content px-12 py-8">
		<ExtractionProfileTab bind:profiles={data.profiles} />
	</div>

	<input name="my_tabs_2" class="tab checked:text-primary" aria-label="Tab 2" type="radio" />
	<div class="tab-content px-6 py-4">Tab content 2</div>

	<input name="my_tabs_2" class="tab checked:text-primary" aria-label="Tab 3" type="radio" />
	<div class="tab-content px-6 py-4">Tab content 3</div>
</div>

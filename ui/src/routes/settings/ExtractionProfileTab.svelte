<script lang="ts">
	import { extractionService } from '$lib/api/extraction.service';
	import type { ExtractionProfile } from '$lib/api/gen/types';
	import Button from '$lib/basics/Button.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';
	import { Save } from '@lucide/svelte';
	import ExtractionProfileDisplay from './ExtractionProfileDisplay.svelte';

	let { profiles = $bindable() }: { profiles: ExtractionProfile[] } = $props();

	let scraperState = $state('');
	async function save() {
		await extractionService.create(JSON.parse(scraperState));
	}

	const example: ExtractionProfile = {
		name: 'Example',
		validUrls: ['https://some.vendor.com'],
		settings: [
			{
				field: 'title',
				selector: '.title',
				regex: undefined,
				operations: [],
				grabAll: false
			},
			{
				field: 'description',
				selector: '.description p',
				regex: undefined,
				operations: [],
				grabAll: true
			},
			{
				field: 'origin',
				selector: 'b:contains(Origin)',
				regex: '(?::\\s*)?(.*)',
				operations: ['nextSibling'],
				grabAll: false
			},
			{
				field: 'harvest',
				selector: 'b:contains(Harvest)',
				regex: '(?::\\s*)?(.*)',
				operations: ['nextSibling'],
				grabAll: false
			},
			{
				field: 'cultivar',
				selector: 'strong:contains(Cultivar)',
				regex: '(?i)(?<=Tea Cultivar:\\s)(.*)',
				operations: [],
				grabAll: false
			}
		]
	};
</script>

<div class="flex flex-col gap-8">
	<div class="prose">
		<h2>Add Scraper (Temporary)</h2>
	</div>
	<Textarea textareaClass="w-full" bind:value={scraperState} />
	<Button class="btn-primary" icon={Save} label="Save" onclick={save} />

	{#each [example, ...profiles] as profile (profile.name)}
		<ExtractionProfileDisplay {profile} />
	{/each}
</div>

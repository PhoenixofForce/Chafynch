<script lang="ts">
	import { extractionService } from '$lib/api/extraction.service';
	import Button from '$lib/basics/Button.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';

	let scraperState = $state('');
	async function save() {
		await extractionService.create(JSON.parse(scraperState));
	}
</script>

<div class="flex w-full flex-col gap-8 p-8">
	<div class="prose">
		<h2>Add Scraper (Temporary)</h2>
	</div>
	<Textarea textareaClass="w-full" bind:value={scraperState} />
	<Button label="Save" onclick={save} />
</div>

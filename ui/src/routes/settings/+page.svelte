<script lang="ts">
	import { extractionService } from '$lib/api/extraction.service';
	import Button from '$lib/basics/Button.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';

	let scraperState = $state('');
	async function save() {
		await extractionService.create(JSON.parse(scraperState));
	}

	const example = `{
    "name": "Vendor X",
    "validUrls": [
      "https://some.vendor.com"
    ],
    "settings": [
      {
        "field": "title",
        "selector": ".title",
        "regex": null,
        "operations": [],
        "grabAll": false
      },
      {
        "field": "description",
        "selector": ".description p",
        "regex": null,
        "operations": [],
        "grabAll": true
      },
      {
        "field": "origin",
        "selector": "b:contains(Origin)",
        "regex": "(?::\\s*)?(.*)",
        "operations": ["nextSibling"],
        "grabAll": false
      },
      {
        "field": "harvest",
        "selector": "b:contains(Harvest)",
        "regex": "(?::\\s*)?(.*)",
        "operations": ["nextSibling"],
        "grabAll": false
      },
      {
        "field": "cultivar",
        "selector": "strong:contains(Cultivar)",
        "regex": "(?i)(?<=Tea Cultivar:\\s)(.*)",
        "operations": [],
        "grabAll": false
      }
    ]
  }`;
</script>

<!-- name of each tab group should be unique -->
<div class="tabs-border tabs w-full">
	<input
		type="radio"
		name="my_tabs_2"
		class="tab checked:text-primary"
		aria-label="Scraper Settings"
		checked={true}
	/>
	<div class="tab-content px-12 py-8">
		<div class="flex flex-col gap-8">
			<div class="prose">
				<h2>Add Scraper (Temporary)</h2>
			</div>
			<Textarea textareaClass="w-full" bind:value={scraperState} />
			<Button label="Save" onclick={save} />

			<div class="prose">
				<h3>Example</h3>
			</div>

			<div class="mockup-code h-86 w-full overflow-y-scroll">
				{#each example.split('\n') as line, i (i)}
					<pre data-prefix={i + 1}><code>{line}</code></pre>
				{/each}
			</div>
		</div>
	</div>

	<input type="radio" name="my_tabs_2" class="tab checked:text-primary" aria-label="Tab 2" />
	<div class="tab-content px-6 py-4">Tab content 2</div>

	<input type="radio" name="my_tabs_2" class="tab checked:text-primary" aria-label="Tab 3" />
	<div class="tab-content px-6 py-4">Tab content 3</div>
</div>

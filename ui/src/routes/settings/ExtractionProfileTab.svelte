<script lang="ts">
	import { extractionService } from '$lib/api/extraction.service';
	import type { ExtractionProfile, ExtractionResult } from '$lib/api/gen/types';
	import Button from '$lib/basics/Button.svelte';
	import Checkbox from '$lib/basics/Checkbox.svelte';
	import Input from '$lib/basics/Input.svelte';
	import Tabs from '$lib/basics/Tabs.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';
	import { Save, Send } from '@lucide/svelte';

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

	let isTestRunning = $state(false);
	let testUrl = $state('');
	let testResult = $state<ExtractionResult | undefined>(undefined);

	async function testProfile(profile: ExtractionProfile) {
		if (!testUrl || isTestRunning) return;

		isTestRunning = true;
		await extractionService
			.extractWithProfile(testUrl, profile)
			.then((res) => (testResult = res))
			.finally(() => (isTestRunning = false));
	}
</script>

<div class="flex flex-col gap-8">
	<div class="prose">
		<h2>Add Scraper (Temporary)</h2>
	</div>
	<Textarea textareaClass="w-full" bind:value={scraperState} />
	<Button class="btn-primary" icon={Save} label="Save" onclick={save} />

	{#each [example, ...profiles] as profile (profile.name)}
		<div class="prose">
			<h3>{profile.name}</h3>
		</div>

		{#if profile.validUrls}
			<div class="prose">
				<h4>Valid Urls</h4>
			</div>

			<ul>
				{#each profile.validUrls as url (url)}
					<li>
						<a class="link" href={url} rel="external noopener noreferrer" target="_blank">{url}</a>
					</li>
				{/each}
			</ul>
		{/if}

		<div class="flex w-full gap-2">
			<div class="flex-1">
				<Input
					disabled={isTestRunning}
					inputClass="w-full"
					placeholder="Url to test"
					bind:value={testUrl}
				/>
			</div>
			<Button
				class="min-w-42 btn-accent"
				disabled={!testUrl}
				icon={Send}
				label="Test {profile.name}"
				loading={isTestRunning}
				onclick={() => testProfile(profile)}
			/>
		</div>

		<Tabs
			class="tabs-border"
			contentClass="border-base-300 bg-base-100"
			tabs={profile.settings?.map((e) => e.field!) ?? []}
		>
			{#snippet renderTab(tab)}
				{@const setting = profile.settings!.find((e) => e.field === tab.id)!}
				<div class="flex flex-col gap-4 md:flex-row">
					<div class="mt-4 flex flex-1 flex-col gap-4">
						<Input
							disabled
							inputClass="w-full"
							placeholder="Selector"
							bind:value={setting.selector}
						/>
						<Input disabled inputClass="w-full" placeholder="Regex" bind:value={setting.regex} />
						<Checkbox disabled label="Grab all?" bind:value={setting.grabAll} />

						<ul>
							{#each setting.operations as operation, i (i)}
								<li>{operation}</li>
							{/each}
						</ul>
					</div>

					<div class="mt-4 flex flex-1 flex-col gap-4">
						{#if testResult}
							{@const result = testResult.details!.find((e) => e.fieldName === tab.id)}
							<div>
								<b>Extraced: </b>
								{result?.fieldValue ?? null}
							</div>
							{#if result?.errors}
								<b>{result.errors.length} Errors:</b>
								<ul>
									{#each result.errors as error (error)}
										<li>
											<span class="badge badge-error">{error}</span>
										</li>
									{/each}
								</ul>
							{/if}
						{/if}
					</div>
				</div>
			{/snippet}
		</Tabs>

		{#if testResult}
			<div class="prose">
				<h4>Raw result</h4>
			</div>

			<div class="mockup-code h-86 w-full overflow-y-scroll">
				{#each JSON.stringify(testResult, null, 4).split('\n') as line, i (i)}
					<pre data-prefix={i + 1}><code>{line}</code></pre>
				{/each}
			</div>
		{/if}

		<!--
		<div class="mockup-code h-86 w-full overflow-y-scroll">
			{#each JSON.stringify(profile, null, 4).split('\n') as line, i (i)}
				<pre data-prefix={i + 1}><code>{line}</code></pre>
			{/each}
		</div>
		-->
	{/each}
</div>

<script lang="ts">
	import { extractionService } from '$lib/api/extraction.service';
	import type { ExtractionProfile, ExtractionResult } from '$lib/api/gen/types';
	import Button from '$lib/basics/Button.svelte';
	import Checkbox from '$lib/basics/Checkbox.svelte';
	import Input from '$lib/basics/Input.svelte';
	import Select from '$lib/basics/Select.svelte';
	import Tabs from '$lib/basics/Tabs.svelte';
	import BasicEntityCard from '$lib/crud/BasicEntityCard.svelte';
	import type { createEditor } from '$lib/crud/editable.svelte';
	import { toast } from '$lib/toast/toast.store.svelte';
	import { Plus, Send, X } from '@lucide/svelte';

	/*
		This is a huge component that needs to be broken down into
		- Url component
		- TabSettings Component
		- TabTestResults Component

		Also basics
		- dropdown
		- a plain button "base style"
		- the operation + valid url pattern

		Also need to add
		- import/ export json
		- import from url
	*/

	let {
		profile = $bindable(),
		editor
	}: { profile: ExtractionProfile; editor: ReturnType<typeof createEditor<ExtractionProfile>> } =
		$props();

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

	// Todo:
	async function onDelete(profile: ExtractionProfile) {
		await extractionService.delete(profile);
		return toast.success(`Successfully created '${profile.name}'`);
	}
	async function onSave(profile: ExtractionProfile, isNew: boolean) {
		if (isNew) {
			await extractionService.create(profile);
			return toast.success(`Successfully created '${profile.name}'`);
		}

		await extractionService.update(profile);
		return toast.success(`Successfully updated '${profile.name}'`);
	}

	const validFields = ['title', 'description', 'origin', 'harvest', 'cultivar'];
	const unsetFields = $derived(
		validFields.filter((prop) => !profile.settings?.some((s) => s.field === prop))
	);

	function addField(profile: ExtractionProfile, field: string) {
		profile.settings = profile.settings ?? [];
		profile.settings!.push({ field });
	}

	const operations = ['nextSibling', 'nextElementSibling'];
</script>

<BasicEntityCard {editor} entity={profile} {onDelete} {onSave}>
	{#snippet title(entity, editing)}
		{#if editing}
			<div class="mr-4 flex-1">
				<Input inputClass="w-full" placeholder="Profile Name" bind:value={entity.name} />
			</div>
		{:else}
			<div class="prose">
				<h3>{entity.name}</h3>
			</div>
		{/if}
	{/snippet}

	{#snippet header(entity, editing)}
		{@const tabs = entity.settings?.map((e) => e.field!) ?? []}

		<div class="prose">
			<h4>Valid Urls</h4>
		</div>

		{#if editing}
			<div class="flex w-full flex-col gap-2">
				{#each entity.validUrls, i}
					<div class="flex w-full gap-2">
						<Button class="btn-error" icon={X} onclick={() => entity.validUrls!.splice(i, 1)} />
						<div class="flex-1">
							<Input inputClass="w-full" bind:value={entity.validUrls![i]} />
						</div>
					</div>
				{/each}
			</div>
			<Button
				class="mb-4 w-full btn-neutral"
				label="Add Url"
				onclick={() => {
					entity.validUrls = entity.validUrls ?? [];
					entity.validUrls.push('');
				}}
			/>
		{:else}
			{#if entity.validUrls}
				<ul>
					{#each entity.validUrls as url, i (i)}
						<li>
							<a class="link" href={url} rel="external noopener noreferrer" target="_blank">
								{url}
							</a>
						</li>
					{/each}
				</ul>
			{/if}
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
				label="Test {entity.name}"
				loading={isTestRunning}
				onclick={() => testProfile(entity)}
			/>
		</div>

		<!-- Todo: make deletable -->
		<Tabs class="tabs-border" contentClass="border-base-300 bg-base-100" {tabs}>
			{#if editing && unsetFields.length}
				<button style="anchor-name:--anchor-1" class="tab" popovertarget="popover-1" type="button">
					<Plus />
				</button>
				<ul
					id="popover-1"
					style="position-anchor:--anchor-1"
					class="menu dropdown w-52 rounded-box bg-base-100 shadow-sm"
					popover
				>
					{#each unsetFields as field (field)}
						<li>
							<button class="capitalize" onclick={() => addField(entity, field)} type="button">
								{field}
							</button>
						</li>
					{/each}
				</ul>
			{/if}

			{#snippet renderTab(tab)}
				{@const setting = entity.settings!.find((e) => e.field === tab.id)!}
				<div class="flex flex-col gap-4 md:flex-row">
					<div class="mt-4 flex flex-1 flex-col gap-4">
						<Input
							disabled={!editing}
							inputClass="w-full"
							placeholder="Selector"
							bind:value={setting.selector}
						/>
						<Input
							disabled={!editing}
							inputClass="w-full"
							placeholder="Regex"
							bind:value={setting.regex}
						/>
						<Checkbox disabled={!editing} label="Grab all?" bind:value={setting.grabAll} />

						{#if editing}
							<div class="flex w-full flex-col gap-2">
								{#each setting.operations, i}
									<div class="flex w-full gap-2">
										<Button
											class="btn-error"
											icon={X}
											onclick={() => setting.operations!.splice(i, 1)}
										/>
										<div class="flex-1">
											<Select
												class="w-full"
												options={operations.map((op) => ({
													value: op,
													label: op
												}))}
												bind:value={setting.operations![i]}
											/>
										</div>
									</div>
								{/each}
							</div>
							<Button
								class="mb-4 w-full btn-neutral"
								label="Add Operation"
								onclick={() => {
									setting.operations = setting.operations ?? [];
									setting.operations.push(operations[0]);
								}}
							/>
						{:else}
							{#if setting.operations?.length}
								<ul>
									{#each setting.operations as operation, i (i)}
										<li>{operation}</li>
									{/each}
								</ul>
							{/if}
						{/if}
					</div>

					<div class="mt-4 flex flex-1 flex-col gap-4 {!testResult ? 'hidden md:block' : ''}">
						{#if testResult}
							{@const result = testResult.details!.find((e) => e.fieldName === tab.id)}
							<div>
								<b>Extraced: </b>
								{result?.fieldValue ?? 'null'}
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
	{/snippet}
</BasicEntityCard>

<!--
<div class="mockup-code h-86 w-full overflow-y-scroll">
	{#each JSON.stringify(profile, null, 4).split('\n') as line, i (i)}
		<pre data-prefix={i + 1}><code>{line}</code></pre>
	{/each}
</div>
-->

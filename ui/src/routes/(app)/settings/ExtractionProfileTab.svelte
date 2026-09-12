<script lang="ts">
	import type { ExtractionProfile } from '$lib/api/gen/types';
	import Button from '$lib/basics/Button.svelte';
	import { Plus } from '@lucide/svelte';
	import ExtractionProfileDisplay from './ExtractionProfileDisplay.svelte';
	import { createEditor } from '$lib/crud/editable.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { toast } from '$lib/toast/toast.store.svelte';

	let { profiles = $bindable() }: { profiles: ExtractionProfile[] } = $props();
	let importProfileJson = $state('');

	const editor = createEditor<ExtractionProfile>();
	function create() {
		let baseProfile: Omit<ExtractionProfile, 'id'> = { name: '' };
		try {
			if (importProfileJson.length) {
				baseProfile = JSON.parse(importProfileJson);
				importProfileJson = '';
			}
		} catch (_) {
			toast.warning('Could not parse JSON.');
			baseProfile = { name: '' };
		}

		if (!baseProfile.validUrls) baseProfile.validUrls = [];
		if (!baseProfile.settings) baseProfile.settings = [];

		const draft: ExtractionProfile = {
			...baseProfile,
			id: -2
		};
		editor.create(draft);
	}
</script>

<div class="flex flex-col gap-8">
	{#if !editor.isNew}
		<Input inputClass="w-full" placeholder="Paste exported JSON" bind:value={importProfileJson} />
		<Button
			class="btn btn-dash btn-primary"
			disabled={editor.editingAny()}
			icon={Plus}
			label={importProfileJson ? 'Import Extraction Profile' : 'Add new Extraction Profile'}
			onclick={create}
		/>
	{:else}
		<ExtractionProfileDisplay {editor} bind:profile={editor.draft!} />
	{/if}

	{#each profiles, i}
		<ExtractionProfileDisplay {editor} profile={profiles[i]} />
	{/each}
</div>

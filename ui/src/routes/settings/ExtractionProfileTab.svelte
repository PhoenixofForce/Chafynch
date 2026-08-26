<script lang="ts">
	import type { ExtractionProfile } from '$lib/api/gen/types';
	import Button from '$lib/basics/Button.svelte';
	import { Plus } from '@lucide/svelte';
	import ExtractionProfileDisplay from './ExtractionProfileDisplay.svelte';
	import { createEditor } from '$lib/crud/editable.svelte';

	let { profiles = $bindable() }: { profiles: ExtractionProfile[] } = $props();

	const editor = createEditor<ExtractionProfile>();
	function create() {
		const draft: ExtractionProfile = {
			id: -2,
			name: ''
		};
		editor.create(draft);
	}
</script>

<div class="flex flex-col gap-8">
	{#if !editor.isNew}
		<Button
			class="btn btn-dash btn-primary"
			disabled={editor.editingAny()}
			icon={Plus}
			label="Add new Extraction Profile"
			onclick={create}
		/>
	{:else}
		<ExtractionProfileDisplay {editor} bind:profile={editor.draft!} />
	{/if}

	{#each profiles, i}
		<ExtractionProfileDisplay {editor} bind:profile={profiles[i]} />
	{/each}
</div>

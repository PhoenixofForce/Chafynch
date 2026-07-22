<script lang="ts">
	import type { CultivarDto } from '$lib/api/gen/types.js';
	import BasicEntityCard from '$lib/crud/BasicEntityCard.svelte';
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { createEditor } from '$lib/crud/editable.svelte.js';
	import { Plus } from '@lucide/svelte';
	import { cultivarService } from '$lib/api/cultivar.service.js';
	import { toast } from '$lib/toast/toast.store.svelte.js';

	const { data } = $props();
	const editor = createEditor<CultivarDto>();

	function create() {
		const draft: CultivarDto = {
			id: -1,
			name: ''
		};
		editor.create(draft);
	}

	async function onSave(cultivar: CultivarDto, isNew: boolean) {
		if (isNew) {
			await cultivarService.create(cultivar.name);
			return toast.success(`Successfully created '${cultivar.name}'`);
		}

		await cultivarService.update(cultivar);
		return toast.success(`Successfully updated '${cultivar.name}'`);
	}

	async function onDelete(cultivar: CultivarDto) {
		await cultivarService.delete(cultivar.id);
		toast.success(`Successfully deleted '${cultivar.name}'`);
	}
</script>

{#snippet editTitle(draft: CultivarDto)}
	<Input required placeholder="Name*" bind:value={draft.name} hint="Name is required" />
{/snippet}

<div class="flex w-full flex-col gap-8 p-8">
	<div class="prose">
		<h2>Manage Cultivars</h2>
	</div>
	{#if !editor.isNew}
		<Button
			class="btn btn-dash btn-primary"
			onclick={create}
			label="Add Cultivar"
			icon={Plus}
			disabled={editor.editingAny()}
		/>
	{/if}

	{#if editor.isNew}
		<BasicEntityCard entity={editor.draft!} {editor} {onSave} {onDelete} {editTitle}>
			{#snippet title()}
				<div class="text-lg font-bold">New Cultivar</div>
			{/snippet}
		</BasicEntityCard>
	{/if}

	{#each data.cultivars as cultivar (cultivar.id)}
		<BasicEntityCard entity={cultivar} {editor} {onSave} {onDelete} {editTitle}>
			{#snippet title()}
				<div class="m-0 text-lg font-bold">{cultivar.name}</div>
			{/snippet}
		</BasicEntityCard>
	{/each}
</div>

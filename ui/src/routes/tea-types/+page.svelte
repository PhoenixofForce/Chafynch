<script lang="ts">
	import type { TeaTypeDto } from '$lib/api/gen/types.js';
	import BasicEntityCard from '$lib/crud/BasicEntityCard.svelte';
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { createEditor } from '$lib/crud/editable.svelte.js';
	import { Plus } from '@lucide/svelte';
	import { teaTypeService } from '$lib/api/teaType.service.js';
	import { toast } from '$lib/toast/toast.store.svelte.js';

	const { data } = $props();
	const editor = createEditor<TeaTypeDto>();

	function create() {
		const draft: TeaTypeDto = {
			id: -1,
			name: ''
		};
		editor.create(draft);
	}

	async function onSave(teaType: TeaTypeDto, isNew: boolean) {
		if (isNew) {
			await teaTypeService.create(teaType);
			return toast.success(`Successfully created '${teaType.name}'`);
		}

		await teaTypeService.update(teaType);
		return toast.success(`Successfully updated '${teaType.name}'`);
	}

	async function onDelete(teaType: TeaTypeDto) {
		await teaTypeService.delete(teaType.id);
		toast.success(`Successfully deleted '${teaType.name}'`);
	}
</script>

{#snippet editTitle(draft: TeaTypeDto)}
	<Input required placeholder="Name*" bind:value={draft.name} hint="Name is required" />
{/snippet}

<div class="flex w-full flex-col gap-8 p-8">
	<div class="prose">
		<h2>Manage Tea Types</h2>
	</div>

	{#if !editor.isNew}
		<Button
			class="btn btn-dash btn-primary"
			onclick={create}
			label="Add Tea Type"
			icon={Plus}
			disabled={editor.editingAny()}
		/>
	{/if}

	{#if editor.isNew}
		<BasicEntityCard entity={editor.draft!} {editor} {onSave} {onDelete} {editTitle}>
			{#snippet title()}
				<div class="text-lg font-bold">New Tea Type</div>
			{/snippet}
		</BasicEntityCard>
	{/if}

	{#each data.types as type (type.id)}
		<BasicEntityCard entity={type} {editor} {onSave} {onDelete} {editTitle}>
			{#snippet title()}
				<div class="m-0 text-lg font-bold">{type.name}</div>
			{/snippet}
		</BasicEntityCard>
	{/each}
</div>

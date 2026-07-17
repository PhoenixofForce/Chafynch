<script lang="ts">
	import { api, wrapApi } from '$lib/api/client.js';
	import type { TeaTypeDto } from '$lib/api/types.js';
	import BasicEntityCard from '$lib/crud/BasicEntityCard.svelte';
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { createEditor } from '$lib/crud/editable.svelte.js';
	import { Plus } from '@lucide/svelte';

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
			return wrapApi(
				() => api.POST('/api/tea-types', { params: { query: { name: teaType.name } } }),
				{
					success: `Successfully created tea type '${teaType.name}'`
				}
			);
		}

		return wrapApi(
			() => api.PUT('/api/tea-types/{id}', { body: teaType, params: { path: { id: teaType.id } } }),
			{
				success: `Successfully updated tea type '${teaType.name}'`
			}
		);
	}

	async function onDelete(teaType: TeaTypeDto) {
		return wrapApi(
			() => api.DELETE('/api/tea-types/{id}', { params: { path: { id: teaType.id } } }),
			{
				success: `Successfully deleted tea type '${teaType.name}'`
			}
		);
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

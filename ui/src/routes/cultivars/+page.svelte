<script lang="ts">
	import { api, wrapApi } from '$lib/api/client.js';
	import type { CultivarDto } from '$lib/api/types.js';
	import BasicEntityCard from '$lib/crud/BasicEntityCard.svelte';
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { createEditor } from '$lib/crud/editable.svelte.js';
	import { Plus } from '@lucide/svelte';

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
			return wrapApi(
				() => api.POST('/api/cultivars', { params: { query: { name: cultivar.name } } }),
				{
					success: `Successfully created cultivar '${cultivar.name}'`
				}
			);
		}

		return wrapApi(
			() =>
				api.PUT('/api/cultivars/{id}', { body: cultivar, params: { path: { id: cultivar.id } } }),
			{
				success: `Successfully updated cultivar '${cultivar.name}'`
			}
		);
	}

	async function onDelete(cultivar: CultivarDto) {
		return wrapApi(
			() => api.DELETE('/api/cultivars/{id}', { params: { path: { id: cultivar.id } } }),
			{
				success: `Successfully deleted cultivar '${cultivar.name}'`
			}
		);
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

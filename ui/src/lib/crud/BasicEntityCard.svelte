<script generics="T extends {id: number}" lang="ts">
	import type { createEditor } from '$lib/crud/editable.svelte';
	import type { Snippet } from 'svelte';
	import { Check, Pen, Trash, X } from '@lucide/svelte';
	import Button from '../basics/Button.svelte';
	import { confirmation } from '../confirmation/confirmation.store.svelte';

	type Props = {
		entity: T;
		editor: ReturnType<typeof createEditor<T>>;
		onSave: (t: T, isNew: boolean) => void;
		onDelete: (t: T) => void;

		title: Snippet<[]>;
		editTitle: Snippet<[T]>;
		header?: Snippet<[]>;
		editHeader?: Snippet<[T]>;
		body?: Snippet<[]>;
	};

	const { entity, editor, onSave, onDelete, title, editTitle, header, editHeader, body }: Props =
		$props();

	let formEl: HTMLFormElement;

	let editing = $derived(editor.isEditing(entity));
	let saving = $state(false);
	let deleting = $state(false);

	async function handleSave() {
		if (!formEl.reportValidity()) return;

		saving = true;
		try {
			await editor.save(onSave);
		} finally {
			saving = false;
		}
	}

	async function handleDelete() {
		confirmation.show({
			title: 'Do you really want to delete this?',
			confirm: {
				label: 'Delete',
				class: 'btn-error',
				onclick: async () => {
					deleting = true;
					try {
						await editor.delete(entity, onDelete);
						confirmation.hide();
					} finally {
						deleting = false;
					}
				}
			}
		});
	}
</script>

<div class="card w-full bg-base-200 shadow">
	<div class="card-body">
		<form bind:this={formEl}>
			<div class="flex {editing ? 'items-start' : 'items-center'} justify-between">
				{#if editing}
					{@render editTitle(editor.draft!)}
				{:else}
					{@render title()}
				{/if}
				<div>
					{#if !editing}
						<Button
							disabled={editor.editingAny() || editor.isPending}
							onclick={() => editor.edit(entity)}
							icon={Pen}
						/>
						<Button
							disabled={editor.editingAny() || editor.isPending}
							onclick={handleDelete}
							class="btn-error"
							icon={Trash}
							loading={deleting}
						/>
					{:else}
						<Button
							disabled={editor.isPending}
							onclick={handleSave}
							class=" btn-success"
							loading={saving}
							icon={Check}
						/>
						<Button
							disabled={editor.isPending}
							onclick={() => editor.cancel()}
							class="btn-error"
							icon={X}
						/>
					{/if}
				</div>
			</div>

			{#if editing && editHeader}
				{@render editHeader(editor.draft!)}
			{:else if header}
				{@render header()}
			{/if}
		</form>

		{#if body}
			{@render body()}
		{/if}
	</div>
</div>

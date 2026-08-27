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

		title: Snippet<[T, boolean]>;
		header?: Snippet<[T, boolean]>;
		body?: Snippet<[T, boolean]>;
	};

	const { entity, editor, onSave, onDelete, title, header, body }: Props = $props();

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
		<form bind:this={formEl} class="flex flex-col gap-4">
			<div class="flex {editing ? 'items-start' : 'items-center'} justify-between">
				{@render title(editing ? editor.draft! : entity, editing)}
				<div>
					{#if !editing}
						<Button
							disabled={editor.editingAny() || editor.isPending}
							icon={Pen}
							onclick={() => editor.edit(entity)}
						/>
						<Button
							class="btn-error"
							disabled={editor.editingAny() || editor.isPending}
							icon={Trash}
							loading={deleting}
							onclick={handleDelete}
						/>
					{:else}
						<Button
							class=" btn-success"
							disabled={editor.isPending}
							icon={Check}
							loading={saving}
							onclick={handleSave}
						/>
						<Button
							class="btn-error"
							disabled={editor.isPending}
							icon={X}
							onclick={() => editor.cancel()}
						/>
					{/if}
				</div>
			</div>

			{@render header?.(editing ? editor.draft! : entity, editing)}
		</form>

		{@render body?.(editing ? editor.draft! : entity, editing)}
	</div>
</div>

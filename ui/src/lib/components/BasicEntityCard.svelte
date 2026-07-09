<script generics="T extends {id: number}" lang="ts">
	import type { createEditor } from '$lib/data/editable.svelte';
	import type { Snippet } from 'svelte';
	import { Check, Pen, Trash, X } from '@lucide/svelte';
	import Button from './ui/Button.svelte';

	type Props = {
		entity: T;
		editor: ReturnType<typeof createEditor<T>>;
		onSave: (t: T, isNew: boolean) => void | Promise<void>;
		onDelete: (t: T) => void | Promise<void>;

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
		deleting = true;
		try {
			await editor.delete(entity, onDelete);
		} finally {
			deleting = false;
		}
	}
</script>

<div class="card mb-8 w-full bg-base-200 shadow">
	<div class="card-body">
		<form bind:this={formEl}>
			<div class="flex items-center justify-between">
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

			{#if body}
				{@render body()}
			{/if}
		</form>
	</div>
</div>

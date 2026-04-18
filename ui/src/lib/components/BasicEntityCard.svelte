<script generics="T extends {id: number}" lang="ts">
	import type { createEditor } from '$lib/data/editable.svelte';
	import type { Snippet } from 'svelte';
	import { Check, Pen, Trash, X } from '@lucide/svelte';

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

	let editing = $derived(editor.isEditing(entity));
	let saving = $state(false);
	let deleting = $state(false);

	async function handleSave() {
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
		<div class="flex items-center justify-between">
			{#if editing}
				{@render editTitle(editor.draft!)}
			{:else}
				{@render title()}
			{/if}
			<div>
				{#if !editing}
					<button
						disabled={editor.editingAny() || editor.isPending}
						onclick={() => editor.edit(entity)}
						class="btn btn-square"
					>
						<Pen />
					</button>
					<button
						disabled={editor.editingAny() || editor.isPending}
						onclick={handleDelete}
						class="btn btn-square btn-error"
					>
						{#if !deleting}
							<Trash />
						{:else}
							<span class="loading loading-sm loading-ring"></span>
						{/if}
					</button>
				{:else}
					<button
						disabled={editor.isPending}
						onclick={handleSave}
						class="btn btn-square btn-success"
					>
						{#if !saving}
							<Check />
						{:else}
							<span class="loading loading-sm loading-ring"></span>
						{/if}
					</button>
					<button
						disabled={editor.isPending}
						onclick={() => editor.cancel()}
						class="btn btn-square btn-error"><X /></button
					>
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
	</div>
</div>

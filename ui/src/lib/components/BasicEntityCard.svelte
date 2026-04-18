<script generics="T extends {id: number}" lang="ts">
	import type { createEditor } from '$lib/data/editable.svelte';
	import type { Snippet } from 'svelte';
	import { Check, Pen, Trash, X } from '@lucide/svelte';

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

	let editing = $derived(editor.isEditing(entity));
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
				{#if !editor.isEditing(entity)}
					<button
						disabled={editor.editingAny()}
						onclick={() => editor.edit(entity)}
						class="btn btn-square"
					>
						<Pen />
					</button>
					<button
						disabled={editor.editingAny()}
						onclick={() => onDelete(entity)}
						class="btn btn-square btn-error"
					>
						<Trash />
					</button>
				{:else}
					<button onclick={() => editor.save(onSave)} class="btn btn-square btn-success">
						<Check />
					</button>
					<button onclick={() => editor.cancel()} class="btn btn-square btn-error"><X /></button>
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

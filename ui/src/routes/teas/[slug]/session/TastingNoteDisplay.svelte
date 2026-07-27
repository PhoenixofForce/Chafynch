<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import { Plus } from '@lucide/svelte';
	import type { TastingNoteSubCategory } from './types';

	let { subCategory, openModal }: { subCategory: TastingNoteSubCategory; openModal: () => void } =
		$props();
</script>

<div class="flex w-full items-center gap-4 rounded-full bg-base-200 px-4 py-2">
	<span class="min-w-1/10 text-xs text-base-content/80">{subCategory.name}</span>
	<span class="flex flex-1 gap-2">
		{#each subCategory.notes as tag (tag)}
			<div class="badge badge-xs badge-accent">{tag}</div>
		{/each}
	</span>
	<Button class="h-4 w-4" noAnimation icon={Plus} onclick={openModal} />
</div>

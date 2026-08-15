<script lang="ts">
	import type { TastingNoteDto } from '$lib/api/gen/types';
	import Input from '$lib/basics/Input.svelte';
	import Modal from '$lib/basics/Modal.svelte';
	import { type TastingNoteCategory } from './types';

	let {
		infusion,
		categories
	}: { infusion?: { tastingNotes?: TastingNoteDto[] }; categories: TastingNoteCategory[] } =
		$props();

	let allNotes = ['Smooth', 'Sweet'];

	let filter = $state('');

	let selectedCategoryIndex = $state(0);
	let selectedCategory = $derived(categories[selectedCategoryIndex]);

	let selectedSubCategoryIndex = $state(0);
	let selectedSubCategory = $derived(selectedCategory.subCategories[selectedSubCategoryIndex]);

	let selectedNotes = $derived<TastingNoteDto[]>(
		infusion?.tastingNotes?.filter(
			(e) => e.category === selectedCategory.name && e.subCategory === selectedSubCategory
		) ?? []
	);

	let noteSuggestions = $derived(
		allNotes
			.filter((note) => note.toLowerCase().includes(filter.toLowerCase()))
			.filter((note) => !selectedNotes.some((e) => e.note === note))
	);

	function toggle(tag: string) {
		if (!infusion) return;
		const tastingNote: TastingNoteDto = {
			category: selectedCategory.name,
			subCategory: selectedSubCategory,
			note: tag
		};

		infusion.tastingNotes = infusion.tastingNotes?.some(
			(e) =>
				e.note === tastingNote.note &&
				e.category === tastingNote.category &&
				e.subCategory === tastingNote.subCategory
		)
			? infusion.tastingNotes.filter(
					(e) =>
						!(
							e.note === tastingNote.note &&
							e.category === tastingNote.category &&
							e.subCategory === tastingNote.subCategory
						)
				)
			: [tastingNote, ...(infusion.tastingNotes ?? [])];
	}

	let modalOpen = $state(false);
	export function open(category?: string, subCategory?: string) {
		selectedCategoryIndex = categories.findIndex((e) => e.name === category);

		if (selectedCategoryIndex < 0) {
			selectedCategoryIndex = 0;
			selectedSubCategoryIndex = 0;
		} else {
			selectedSubCategoryIndex = Math.max(
				0,
				selectedCategory.subCategories.findIndex((e) => e === subCategory)
			);
		}

		modalOpen = true;
	}
</script>

<Modal class="modal-bottom sm:modal-middle" bind:open={modalOpen}>
	<div class="flex flex-col gap-4">
		<div class="prose">
			<h3>Add Tasting Notes</h3>
		</div>

		<div class="join w-full" data-testid="Category-Picker">
			{#each categories as category, i (category.name)}
				<input
					name="category"
					class="btn join-item flex-1 btn-md"
					aria-label={category.name}
					checked={selectedCategoryIndex == i}
					onclick={() => {
						selectedCategoryIndex = i;
						selectedSubCategoryIndex = 0;
					}}
					type="radio"
				/>
			{/each}
		</div>

		<div class="join w-full">
			{#each selectedCategory.subCategories as subCategory, i (subCategory)}
				<input
					name="subCategory"
					class="btn join-item flex-1 btn-sm"
					aria-label={subCategory}
					checked={selectedSubCategoryIndex == i}
					onclick={() => (selectedSubCategoryIndex = i)}
					type="radio"
				/>
			{/each}
		</div>

		<div class="prose">
			<h4>Notes</h4>
		</div>

		{#if selectedNotes.length}
			<div class="flex gap-2">
				{#each selectedNotes as tag (tag)}
					<button class="badge badge-accent" onclick={() => toggle(tag.note!)}>{tag.note}</button>
				{/each}
			</div>

			<div class="divider my-0"></div>
		{/if}

		<div class="flex gap-2">
			{#if noteSuggestions.length > 0}
				{#each noteSuggestions as tag (tag)}
					<button class="badge badge-neutral" onclick={() => toggle(tag)}>{tag}</button>
				{/each}
			{:else}
				<span class="text-base-content/60 italic">No suggestions found</span>
			{/if}
		</div>

		<Input
			inputClass="mb-20 w-full sm:mb-0"
			onkeydown={(e) => {
				if (e.key === 'Enter') {
					toggle(filter);
					filter = '';
				}
			}}
			placeholder="Search Note (Enter to add)"
			bind:value={filter}
		/>
	</div>
</Modal>

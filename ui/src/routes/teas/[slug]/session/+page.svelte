<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import SessionBottomBar from './SessionBottomBar.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import TastingNoteModal from './TastingNoteModal.svelte';
	import TimerBar from './TimerBar.svelte';
	import type { Session, TastingNoteCategory } from './types';

	let activeInfusionCounter = $state(0);
	let sessions = $state<Session>({
		infusions: [
			{
				startTime: new Date(),
				infusionTime: 24,
				rating: 3,
				tastingCategories: [
					{
						name: 'Eye',
						subCategories: [{ name: 'Wet Leaf', notes: ['Green', 'Curly'] }]
					}
				]
			}
		]
	});

	let activeInfusion = $derived(sessions.infusions.at(activeInfusionCounter));
	let isTimerRunning = $state(false);

	let tastingNoteModal = $state<ReturnType<typeof TastingNoteModal>>();

	let visibleTastingNoteCategories = $derived<TastingNoteCategory[]>(
		activeInfusion?.tastingCategories.filter((category) =>
			category.subCategories.some((sub) => sub.notes.length > 0)
		) ?? []
	);
</script>

<TastingNoteModal bind:this={tastingNoteModal} infusion={activeInfusion} />
<div class="flex w-full flex-1 flex-col items-center justify-between gap-6">
	<div>header</div>
	<div class="flex w-full flex-1 flex-col gap-2">
		{#if visibleTastingNoteCategories.length > 0}
			{#each visibleTastingNoteCategories as category (category.name)}
				<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>
				{#each category.subCategories.filter((subCategory) => subCategory.notes.length > 0) as subCategory (subCategory.name)}
					<TastingNoteDisplay
						{subCategory}
						openModal={() => tastingNoteModal?.open(category.name, subCategory.name)}
					/>
				{/each}
			{/each}
		{:else}
			<Button
				label="Add your first tasting note"
				class="btn-dash"
				onclick={() => tastingNoteModal?.open()}
			/>
		{/if}
	</div>
	<TimerBar {activeInfusion} bind:isTimerRunning />
	<SessionBottomBar
		disabled={isTimerRunning}
		bind:infusions={sessions.infusions}
		bind:activeInfusion={activeInfusionCounter}
	/>
</div>

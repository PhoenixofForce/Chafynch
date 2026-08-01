<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import SessionBottomBar from './SessionBottomBar.svelte';
	import TastingNoteDisplay from './TastingNoteDisplay.svelte';
	import TastingNoteModal from './TastingNoteModal.svelte';
	import TimerBar from './TimerBar.svelte';
	import { categories, type Session } from './types';

	let activeInfusionCounter = $state(0);
	let sessions = $state<Session>({
		infusions: [
			{
				startTime: new Date(),
				infusionTime: 24,
				rating: 3,
				tastingNotes: {
					'Eye/Wet Leaf': ['Sweet', 'Smooth']
				}
			}
		]
	});

	let activeInfusion = $derived(sessions.infusions.at(activeInfusionCounter));
	let isTimerRunning = $state(false);

	let tastingNoteModal = $state<ReturnType<typeof TastingNoteModal>>();
	let hasNotes = $derived(
		Object.values(activeInfusion?.tastingNotes ?? {}).some((notes) => notes.length > 0)
	);
</script>

<TastingNoteModal bind:this={tastingNoteModal} infusion={activeInfusion} />
<div class="flex min-h-0 w-full flex-1 flex-col items-center justify-between gap-6">
	<div>header</div>
	<div class="flex min-h-0 w-full flex-1 flex-col gap-2 overflow-x-hidden overflow-y-auto">
		{#if hasNotes}
			{#each categories as category (category.name)}
				{#if category.subCategories.some((sub) => (activeInfusion?.tastingNotes[category.name + '/' + sub] ?? []).length > 0)}
					<div class="w-full text-xs text-base-content/50 uppercase">{category.name}</div>
				{/if}

				{#each category.subCategories as subCategory (subCategory)}
					{@const notes = activeInfusion?.tastingNotes[category.name + '/' + subCategory] ?? []}
					{#if notes.length > 0}
						<TastingNoteDisplay
							name={subCategory}
							{notes}
							openModal={() => tastingNoteModal?.open(category.name, subCategory)}
						/>
					{/if}
				{/each}
			{/each}
		{:else}
			<Button
				label="Add your first tasting note"
				class="w-full btn-dash"
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
